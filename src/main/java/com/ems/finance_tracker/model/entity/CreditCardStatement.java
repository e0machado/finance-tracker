package com.ems.finance_tracker.model.entity;

import com.ems.finance_tracker.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing a Credit Card Statement in the financial tracking system.
 * <p>
 * Each statement is associated with a {@link CreditCard} and a reference month,
 * grouping all transactions within that billing cycle.
 * </p>
 *
 * @author Evandro Machado
 * @see com.ems.finance_tracker.model.entity.CreditCardTransaction
 */
@Entity
@Table(name = "credit_card_statements",
        uniqueConstraints = @UniqueConstraint(columnNames = {"reference_month", "credit_card_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    @Min(1)
    @Max(31)
    @Column(name = "closing_day", nullable = false)
    private Integer closingDay;

    @NotNull
    @Min(1)
    @Max(31)
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
        validateDays();
    }

    private void validateDays() {
        int maxDay = referenceMonth.lengthOfMonth();
        if (closingDay < 1 || closingDay > maxDay) {
            throw new BusinessException(
                    String.format("Invalid closing day for %s. The value must be between 1 and %d.",
                            referenceMonth, maxDay));
        }

        if (dueDay < 1 || dueDay > maxDay) {
            throw new BusinessException(
                    String.format("Invalid due day for %s. The value must be between 1 and %d.",
                            referenceMonth, maxDay));
        }

        // TODO: dueDay currently must be in the same month as closingDay.
        // Future implementation will use LocalDate to support due dates in the following month.
        if (dueDay <= closingDay) {
            throw new BusinessException("Due day must be after closing day");
        }
    }

    /**
     * Checks whether the given transaction already belongs to this statement.
     *
     * @param transaction the transaction to check
     * @return {@code true} if the transaction belongs to this statement, {@code false} otherwise
     */
    private boolean belongsToThisStatement(CreditCardTransaction transaction) {
        return this.equals(transaction.getCreditCardStatement());
    }

    /**
     * Adds a transaction to this statement, establishing the bidirectional relationship.
     * <p>
     * If the transaction is already in this statement, the operation is ignored.
     * If the transaction belongs to a different statement, an {@link IllegalStateException} is thrown.
     * </p>
     *
     * @param transaction the transaction to add
     * @throws IllegalArgumentException if the transaction is null
     * @throws IllegalStateException if the transaction already belongs to another statement
     */
    public void addTransaction(CreditCardTransaction transaction) {
        Objects.requireNonNull(transaction, "Transaction must not be null.");

        if (transactions.contains(transaction)) {
            return;
        }

        if (transaction.getCreditCardStatement() != null &&
        !belongsToThisStatement(transaction)) {
            throw new IllegalStateException(
                    String.format("Transaction %d already belongs to statement %d.",
                            transaction.getId(),
                            transaction.getCreditCardStatement().getId())
            );
        }

        transaction.setCreditCardStatement(this);
        transactions.add(transaction);
    }

    /**
     * Removes a transaction from this statement, clearing the bidirectional relationship.
     * <p>
     * If the transaction is not in this statement, the operation is ignored.
     * </p>
     *
     * @param transaction the transaction to remove
     * @throws IllegalArgumentException if the transaction is null
     */
    public void removeTransaction(CreditCardTransaction transaction) {
        Objects.requireNonNull(transaction, "Transaction must not be null.");

        if (!transactions.contains(transaction)) {
            return;
        }

        transaction.setCreditCardStatement(null);
        transactions.remove(transaction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCardStatement other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
