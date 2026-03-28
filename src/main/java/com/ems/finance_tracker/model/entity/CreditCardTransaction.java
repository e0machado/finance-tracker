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

/**
 * Entity representing a Credit Card Transaction in the financial tracking system.
 * <p>
 * Each transaction is associated with a {@link CreditCard}, a {@link Category},
 * and a {@link CreditCardStatement}, and may represent either a purchase or an income,
 * with optional installment support.
 * </p>
 *
 * @author Evandro Machado
 * @see com.ems.finance_tracker.model.enums.CreditCardTransactionType
 */
@Entity
@Table(name = "credit_card_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"creditCard", "category", "creditCardStatement"})
@Builder
public class CreditCardTransaction {

    private static final LocalDate MIN_PURCHASE_DATE = LocalDate.of(2020, 1, 1);

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id", nullable = false)
    private CreditCard creditCard;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statement_id", nullable = false)
    private CreditCardStatement creditCardStatement;

    // TODO: Restrict setters on installment fields (isInstallment, currentInstallment, totalInstallments)
    // and expose explicit domain methods (e.g. configureAsInstallment, configureAsSingleInstallment)
    // to prevent invalid state mutations and align with the rich domain model adopted in CreditCard.
    @PrePersist
    @PreUpdate
    private void validate() {
        validateInstallments();
        validatePurchaseDate();
    }

    /**
     * Validates installment fields before persistence.
     * <p>
     * For non-installment transactions, resets {@code currentInstallment} and
     * {@code totalInstallments} to 1. For installment transactions, ensures
     * {@code totalInstallments} is greater than 1 and that {@code currentInstallment}
     * does not exceed {@code totalInstallments}.
     * </p>
     */
    private void validateInstallments() {

        if (!isInstallment) {
            this.currentInstallment = 1;
            this.totalInstallments = 1;
        } else if (totalInstallments <= 1) {
            throw new BusinessException("Installments must be greater than 1.");
        }
        if (currentInstallment > totalInstallments) {
            throw new BusinessException("Current installment cannot be greater than total installments.");
        }
    }

    /**
     * Validates the purchase date against the allowed date range.
     * <p>
     * The minimum allowed date is {@link #MIN_PURCHASE_DATE}.
     * The maximum allowed date is 365 days from today.
     * </p>
     */
    private void validatePurchaseDate() {
        LocalDate maxReasonableDate = LocalDate.now().plusDays(365);

        if (purchaseDate.isBefore(MIN_PURCHASE_DATE)) {
            throw new BusinessException(
                    String.format("Purchase date %s is too old. Minimum allowed: %s.",
                            purchaseDate, MIN_PURCHASE_DATE)
            );
        }

        if (purchaseDate.isAfter(maxReasonableDate)) {
            throw new BusinessException(
                    String.format("Purchase date %s exceeds the maximum allowed date of %s.",
                            purchaseDate, maxReasonableDate)
            );
        }

    }

    /**
     * Returns the signed amount of the transaction based on its type.
     * <p>
     * Income transactions return a positive value,
     * while expense transactions return a negative value.
     * </p>
     *
     * @return the signed {@link BigDecimal} amount
     */
    @Transient
    public BigDecimal getSignedAmount() {
        if (type == null) return amount;

        return type.isIncome() ? amount : amount.negate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCardTransaction other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
