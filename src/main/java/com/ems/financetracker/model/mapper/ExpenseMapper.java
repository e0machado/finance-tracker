package com.ems.financetracker.model.mapper;

import com.ems.financetracker.dto.ExpenseDTO;
import com.ems.financetracker.model.entity.CostCenter;
import com.ems.financetracker.model.entity.Expense;
import com.ems.financetracker.model.entity.MonthlyBudget;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper responsible for converting {@link ExpenseDTO} to {@link Expense}
 * and vice versa.
 */
@Component
public class ExpenseMapper {

    /**
     * Converts an expense entry creation request into an entity.
     *
     * <p>Preserves the PENDING status initialized by the entity.</p>
     *
     * @param dto the expense entry creation request data
     * @param budget the associated monthly budget
     * @param costCenter the associated cost center
     * @return a new expense entry entity ready for persistence
     */
    public Expense toEntity(ExpenseDTO.Request dto, MonthlyBudget budget, CostCenter costCenter) {
        Expense expense = new Expense();
        expense.setDescription(dto.description());
        expense.setAmount(dto.amount());
        expense.setDueDate(dto.dueDate());
        expense.setFixed(dto.isFixed());
        expense.setComment(dto.comment());
        expense.setMonthlyBudget(budget);
        expense.setCostCenter(costCenter);
        return expense;
    }

    /**
     * Converts an expense entry entity into a response DTO.
     *
     * @param expense the persisted expense entry
     * @return a response DTO exposing expense entry data
     */
    public ExpenseDTO.Response toResponse(Expense expense) {
        return new ExpenseDTO.Response(
                expense.getId(), expense.getDescription(), expense.getAmount(),
                expense.getDueDate(), expense.isFixed(), expense.getComment(), expense.getStatus(),
                new ExpenseDTO.CostCenterRef(expense.getCostCenter().getId()),
                new ExpenseDTO.MonthlyBudgetRef(expense.getMonthlyBudget().getId())
        );
    }

    /**
     * Applies the fields present in the update DTO to the existing expense entry.
     *
     * @param expense the existing expense entry entity
     * @param dto the DTO containing updated information
     * @param costCenter the replacement cost center, or empty to preserve the current association
     */
    public void updateEntity(Expense expense, ExpenseDTO.Update dto, Optional<CostCenter> costCenter) {
        dto.description().ifPresent(expense::setDescription);
        dto.amount().ifPresent(expense::setAmount);
        dto.dueDate().ifPresent(expense::setDueDate);
        dto.isFixed().ifPresent(expense::setFixed);
        dto.comment().ifPresent(expense::setComment);
        costCenter.ifPresent(expense::setCostCenter);
    }
}
