package com.ems.finance_tracker.model.entity;

import com.ems.finance_tracker.exception.BusinessException;
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
 * @see com.ems.finance_tracker.model.entity.User
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
    @Column(name = "closing_day", nullable = false)
    private Integer closingDay;

    @NotNull
    @Min(1)
    @Max(31)
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

    private void validateDays() {
        if (Objects.equals(closingDay, dueDay)) {
            throw new BusinessException("Closing day and due day must not be equal.");
        }
    }

    private void validateAvailableLimit() {
        if (availableLimit.compareTo(creditLimit) > 0) {
            throw new BusinessException("Available limit must not be greater than credit limit.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null.");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be greater than zero.");
        }
    }

    public void addDebit(BigDecimal amount) {
        validateAmount(amount);

        if (amount.compareTo(availableLimit) > 0) {
            throw new BusinessException("Amount must not be greater than available limit.");
        }

        this.availableLimit = this.availableLimit.subtract(amount);
    }

    public void addCredit(BigDecimal amount) {
        validateAmount(amount);
        BigDecimal usedLimit = creditLimit.subtract(availableLimit);

        if (amount.compareTo(usedLimit) > 0) {
            throw new BusinessException("Amount exceeds used credit.");
        }

        this.availableLimit = this.availableLimit.add(amount);
    }

    public void increaseCreditLimit(BigDecimal amount) {
        validateAmount(amount);

        this.creditLimit = this.creditLimit.add(amount);
        this.availableLimit = this.availableLimit.add(amount);
    }

    public void decreaseCreditLimit(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(availableLimit) > 0) {
            throw new BusinessException("Cannot reduce more than available limit.");
        }

        this.creditLimit = this.creditLimit.subtract(amount);
        this.availableLimit = this.availableLimit.subtract(amount);
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
