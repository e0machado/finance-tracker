package com.ems.finance_tracker.model.entity;

import com.ems.finance_tracker.exception.BusinessException;
import com.ems.finance_tracker.model.enums.CreditCardTransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "credit_card_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"creditCard", "category", "creditCardStatement"})
@Builder
public class CreditCardTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private CreditCardTransactionType type;

    @NotNull
    @Positive
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "is_installment", nullable = false)
    private boolean isInstallment = false;

    @NotNull
    @Positive
    @Column(name = "current_installment", nullable = false)
    private Integer currentInstallment = 1;

    @NotNull
    @Positive
    @Column(name = "total_installments", nullable = false)
    private Integer totalInstallments = 1;

    @NotNull
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Size(max = 200)
    @Column(length = 200)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id", nullable = false)
    private CreditCard creditCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statement_id", nullable = false)
    private CreditCardStatement creditCardStatement;

    @PrePersist
    @PreUpdate
    private void validate() {
        validateAmount();
        validateInstallments();
        validatePurchaseDate();
    }

    private void validateAmount() {
        Objects.requireNonNull(amount, "Transaction amount must not be null.");

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException("Transaction amount must not be zero.");
        }

        if (amount.signum() < 0) {
            throw new BusinessException("Transaction amount must be positive.");
        }

    }

    private void validateInstallments() {
        if (!isInstallment) {
            this.currentInstallment = 1;
            this.totalInstallments = 1;
        } else if (totalInstallments <= 1) {
            throw new BusinessException("Installments must be greater than 1.");
        }
        if (currentInstallment > totalInstallments) {
            throw new BusinessException("Current installment cannot be greater than final installment.");
        }
    }

    private void validatePurchaseDate() {
        Objects.requireNonNull(purchaseDate, "Purchase date must not be null.");

        LocalDate minReasonableDate = LocalDate.of(2000, 1, 1);

        if (purchaseDate.isBefore(minReasonableDate)) {
            throw new BusinessException(
                    String.format("Purchase date %s is too old. Minimum allowed: %s.",
                            purchaseDate, minReasonableDate)
            );
        }

    }

    @Transient
    public BigDecimal getSignedAmount() {
        if (type == null) return amount;

        return type.isIncome() ? amount : amount.negate();
    }

}
