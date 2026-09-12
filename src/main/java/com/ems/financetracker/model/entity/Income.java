package com.ems.financetracker.model.entity;

import com.ems.financetracker.exception.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing an Income entry in the financial tracking system.
 * <p>
 * Each income is associated with a {@link MonthlyBudget} and must have a date
 * within the budget's reference month.
 * </p>
 *
 * @author Evandro Machado
 * @see com.ems.financetracker.model.entity.MonthlyBudget
 */
@Entity
@Table(name = "incomes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "monthlyBudget")
@Builder
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String description;

    @NotNull
    @Positive
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "is_fixed", nullable = false)
    private boolean isFixed = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthly_budget_id", nullable = false)
    private MonthlyBudget monthlyBudget;

    @PrePersist
    @PreUpdate
    private void validate() {
        validateDate();
    }

    /**
     * Validates the income date against the reference month of the associated budget.
     * <p>
     * The date must fall within the first and last day of the budget's reference month.
     * </p>
     */
    private void validateDate() {
        LocalDate minimumDate = monthlyBudget.getReferenceMonth().atDay(1);
        LocalDate maximumDate = monthlyBudget.getReferenceMonth().atEndOfMonth();

        if (date.isBefore(minimumDate) || date.isAfter(maximumDate)) {
            throw new BusinessException("Date must be between the first and last day of the month.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Income other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
