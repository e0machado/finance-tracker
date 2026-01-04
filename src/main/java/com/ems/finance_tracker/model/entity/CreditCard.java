package com.ems.finance_tracker.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity representing a Credit Card in the financial tracking system.
 * <p>
 * This class manages credit card details including limits, billing cycles (closing and due days),
 * and tracks the current balance. Each credit card must be associated with a valid {@link User}.
 * </p>
 *
 * @author Evandro Machado <a href="https://github.com/e0machado">Github</a>
 * @see com.ems.finance_tracker.model.entity.User
 */
@Entity
@Table(name = "credit_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@Builder
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
    @Builder.Default
    @Column(name = "current_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
