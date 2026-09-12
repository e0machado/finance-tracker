package com.ems.financetracker.model.entity;

import com.ems.financetracker.exception.BusinessException;
import com.ems.financetracker.model.enums.ExpenseStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Entity representing an Expense entry in the financial tracking system.
 * <p>
 * Each expense is associated with a {@link MonthlyBudget} and a {@link CostCenter},
 * and must have a due date within the budget's reference month.
 * </p>
 *
 * @author Evandro Machado
 * @see com.ems.financetracker.model.enums.ExpenseStatus
 * @see com.ems.financetracker.model.entity.MonthlyBudget
 */
@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"monthlyBudget", "category"})
@Builder
public class Expense {

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
    private LocalDate dueDate;

    @Column(name = "is_fixed", nullable = false)
    private boolean isFixed = false;

    @Size(max = 200)
    @Column(length = 200)
    private String comment;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ExpenseStatus status = ExpenseStatus.PENDING;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_center_id", nullable = false)
    private CostCenter costCenter;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthly_budget_id", nullable = false)
    private MonthlyBudget monthlyBudget;

    @PrePersist
    @PreUpdate
    private void validate() {
        validateDueDate();
    }

    /**
     * Validates the expense due date against the reference month of the associated budget.
     * <p>
     * The due date must fall within the first and last day of the budget's reference month.
     * </p>
     */
    private void validateDueDate() {
        LocalDate minimumDate = monthlyBudget.getReferenceMonth().atDay(1);
        LocalDate maximumDate = monthlyBudget.getReferenceMonth().atEndOfMonth();

        if (dueDate.isBefore(minimumDate) || dueDate.isAfter(maximumDate)) {
            throw new BusinessException("Date must be between the first and last day of the month.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
