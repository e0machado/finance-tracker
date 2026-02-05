package com.ems.finance_tracker.model.entity;

import com.ems.finance_tracker.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "credit_card_statements",
        uniqueConstraints = @UniqueConstraint(columnNames = {"reference_month", "credit_card_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"creditCard", "transactions"})
@Builder
public class CreditCardStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "reference_month", nullable = false, length = 7)
    private YearMonth referenceMonth;

    @NotNull
    @Column(name = "closing_day", nullable = false)
    private Integer closingDay;

    @NotNull
    @Column(name = "due_day", nullable = false)
    private Integer dueDay;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id", nullable = false)
    private CreditCard creditCard;

    @Builder.Default
    @OneToMany(mappedBy = "creditCardStatement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCardTransaction> transactions = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void validate() {
        validateReferenceMonth();
        validateDays();
    }

    private void validateReferenceMonth() {
        Objects.requireNonNull(referenceMonth, "Reference month must not be null.");
    }

    private void validateDays() {
        int maxDay = referenceMonth.lengthOfMonth();
        if (closingDay == null || closingDay < 1 || closingDay > maxDay) {
            throw new BusinessException(
                    String.format("Invalid closing day for %s. The value must be between 1 and %d.",
                            referenceMonth, maxDay));
        }

        if (dueDay == null || dueDay < 1 || dueDay > maxDay) {
            throw new BusinessException(
                    String.format("Invalid due day for %s. The value must be between 1 and %d.",
                            referenceMonth, maxDay));
        }

        if (dueDay <= closingDay) {
            throw new BusinessException("Due day must be after closing day");
        }
    }

    public void addTransaction(CreditCardTransaction transaction) {
        Objects.requireNonNull(transaction, "Transaction must not be null.");

        if (transactions.contains(transaction)) {
            return;
        }

        if (transaction.getCreditCardStatement() != null &&
        !transaction.getCreditCardStatement().equals(this)) {
            throw new IllegalStateException(
                    String.format("Transaction %d already belongs to statement %d.",
                            transaction.getId(),
                            transaction.getCreditCardStatement().getId())
            );
        }

        transactions.add(transaction);
        transaction.setCreditCardStatement(this);

    }

    public void removeTransaction(CreditCardTransaction transaction) {
        Objects.requireNonNull(transaction, "Transaction must not be null.");

        if (!transactions.contains(transaction)) {
            return;
        }

        if (!this.equals(transaction.getCreditCardStatement())) {
            throw new IllegalStateException(
                    String.format("Transaction %d does not belong to statement %d.",
                            transaction.getId(),
                            this.id)
            );
        }

        transactions.remove(transaction);
        transaction.setCreditCardStatement(null);

    }

}
