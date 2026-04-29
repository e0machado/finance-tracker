package com.ems.financetracker.model.entity;

import com.ems.financetracker.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entity representing a Credit Card in the financial tracking system.
 * <p>
 * This class manages credit card details including limits, billing cycles (closing and due days),
 * and tracks the available limit. Each credit card must be associated with a valid {@link User}.
 * </p>
 *
 * @author Evandro Machado
 * @see com.ems.financetracker.model.entity.User
 */
@Entity
@Table(name = "credit_cards",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "user")
@Builder(builderClassName = "CreditCardBuilder", access = AccessLevel.PRIVATE)
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull
    @PositiveOrZero
    @Setter(AccessLevel.NONE)
    @Column(name = "credit_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @NotNull
    @Min(1)
    @Max(31)
    @Setter(AccessLevel.NONE)
    @Column(name = "closing_day", nullable = false)
    private Integer closingDay;

    @NotNull
    @Min(1)
    @Max(31)
    @Setter(AccessLevel.NONE)
    @Column(name = "due_day", nullable = false)
    private Integer dueDay;

    @NotNull
    @PositiveOrZero
    @Setter(AccessLevel.NONE)
    @Column(name = "available_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal availableLimit;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Factory method for creating a new {@link CreditCard} instance.
     * <p>
     * If {@code availableLimit} is not provided, it defaults to {@code creditLimit},
     * assuming the card has no outstanding balance at the time of creation.
     * </p>
     *
     * @param name the card name
     * @param creditLimit the total credit limit
     * @param closingDay the billing cycle closing day
     * @param dueDay the billing cycle due day
     * @param availableLimit the current available limit, or {@code null} to default to {@code creditLimit}
     * @param user the owner of the credit card
     * @return a new {@link CreditCard} instance ready for persistence
     */
    public static CreditCard of(
            String name,
            BigDecimal creditLimit,
            Integer closingDay,
            Integer dueDay,
            BigDecimal availableLimit,
            User user
    ) {
        return CreditCard.builder()
                .name(name)
                .creditLimit(creditLimit)
                .closingDay(closingDay)
                .dueDay(dueDay)
                .availableLimit((availableLimit == null) ? creditLimit : availableLimit)
                .user(user)
                .build();
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        validateDays();
        validateAvailableLimit();
    }

    /**
     * Validates that closing day and due day are not equal.
     *
     * @throws BusinessException if closing day and due day are equal
     */
    private void validateDays() {
        if (Objects.equals(closingDay, dueDay)) {
            throw new BusinessException("Closing day and due day must not be equal.");
        }
    }

    /**
     * Validates that the available limit does not exceed the credit limit.
     *
     * @throws BusinessException if available limit is greater than credit limit
     */
    private void validateAvailableLimit() {
        if (availableLimit.compareTo(creditLimit) > 0) {
            throw new BusinessException("Available limit must not be greater than credit limit.");
        }
    }

    /**
     * Validates that the given amount is not null, zero or negative.
     *
     * @param amount the amount to validate
     * @throws NullPointerException if the amount is null
     * @throws BusinessException if the amount is zero or negative
     */
    private void validateAmount(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null.");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be greater than zero.");
        }
    }

    /**
     * Debits the given amount from the available limit of this credit card.
     *
     * @param amount the amount to debit
     * @throws BusinessException if the amount is null, zero or negative,
     *                           or if it exceeds the available limit
     */
    public void addDebit(BigDecimal amount) {
        validateAmount(amount);

        if (amount.compareTo(availableLimit) > 0) {
            throw new BusinessException("Amount must not be greater than available limit.");
        }

        this.availableLimit = this.availableLimit.subtract(amount);
    }

    /**
     * Credits the given amount back to the available limit of this credit card.
     * <p>
     * The amount cannot exceed the currently used limit.
     * </p>
     *
     * @param amount the amount to credit
     * @throws BusinessException if the amount is null, zero or negative,
     *                           or if it exceeds the used limit
     */
    public void addCredit(BigDecimal amount) {
        validateAmount(amount);
        BigDecimal usedLimit = creditLimit.subtract(availableLimit);

        if (amount.compareTo(usedLimit) > 0) {
            throw new BusinessException("Amount exceeds used credit.");
        }

        this.availableLimit = this.availableLimit.add(amount);
    }

    /**
     * Updates the credit limit of this credit card and adjusts the available limit accordingly.
     * <p>
     * The new limit cannot be lower than the currently used amount.
     * The difference between the new and old limit is applied directly to the available limit.
     * </p>
     *
     * @param newLimit the new credit limit value
     * @throws BusinessException if the new limit is null, zero or negative,
     *                           or if it would fall below the currently used amount
     */
    public void updateCreditLimit(BigDecimal newLimit) {
        validateAmount(newLimit);
        BigDecimal usedLimit = creditLimit.subtract(availableLimit);

        if (newLimit.compareTo(usedLimit) < 0) {
            throw new BusinessException("Cannot reduce credit limit below used amount.");
        }

        BigDecimal difference = newLimit.subtract(creditLimit);
        this.availableLimit = this.availableLimit.add(difference);
        this.creditLimit = newLimit;
    }

    /**
     * Updates the billing cycle of this credit card atomically.
     * <p>
     * Both {@code closingDay} and {@code dueDay} must be provided and must not be equal.
     * This method ensures the billing cycle is always updated as a single consistent operation,
     * preventing intermediate invalid states.
     * </p>
     *
     * @param closingDay the new closing day of the billing cycle
     * @param dueDay the new due day of the billing cycle
     * @throws BusinessException if either value is null or if both values are equal
     */
    public void updateBillingCycle(Integer closingDay, Integer dueDay) {
        if (closingDay == null || dueDay == null) {
            throw new BusinessException("Closing day and due day are required.");
        }

        if (Objects.equals(closingDay, dueDay)) {
            throw new BusinessException("Closing day and due day must not be equal.");
        }

        this.closingDay = closingDay;
        this.dueDay = dueDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCard other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
