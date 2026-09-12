package com.ems.financetracker.service;

import com.ems.financetracker.dto.ExpenseDTO;
import com.ems.financetracker.exception.BusinessException;
import com.ems.financetracker.exception.ResourceNotFoundException;
import com.ems.financetracker.model.entity.CostCenter;
import com.ems.financetracker.model.entity.Expense;
import com.ems.financetracker.model.entity.MonthlyBudget;
import com.ems.financetracker.model.mapper.ExpenseMapper;
import com.ems.financetracker.repository.CostCenterRepository;
import com.ems.financetracker.repository.ExpenseRepository;
import com.ems.financetracker.repository.MonthlyBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing {@link Expense} business operations.
 * Handles validation, persistence coordination and DTO/entity transformations.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final CostCenterRepository costCenterRepository;
    private final ExpenseMapper expenseMapper;

    /**
     * Retrieves all expense entries from the system.
     *
     * @return a list of {@link ExpenseDTO.Response} representing all registered expense entries
     */
    public List<ExpenseDTO.Response> findAllExpenses() {
        return expenseRepository.findAll().stream().map(expenseMapper::toResponse).toList();
    }

    /**
     * Retrieves a single expense entry by its identifier.
     *
     * @param id the expense entry identifier
     * @return a {@link ExpenseDTO.Response} with the expense entry data
     * @throws ResourceNotFoundException if the expense entry does not exist
     */
    public ExpenseDTO.Response findExpenseById(Long id) {
        return expenseMapper.toResponse(findEntityById(id));
    }

    /**
     * Creates and persists a new expense entry.
     * Resolves the budget and cost center and validates the due date against the reference month.
     *
     * @param dto the expense entry creation request data
     * @return a {@link ExpenseDTO.Response} representing the persisted expense entry
     * @throws BusinessException if required data is invalid or the due date is outside the budget month
     * @throws ResourceNotFoundException if an associated resource does not exist
     */
    @Transactional
    public ExpenseDTO.Response saveExpense(ExpenseDTO.Request dto) {
        validateFields(dto.description(), dto.amount(), dto.comment());
        if (dto.monthlyBudget() == null || dto.monthlyBudget().id() == null) {
            throw new BusinessException("Monthly budget ID is required.");
        }
        if (dto.costCenter() == null) {
            throw new BusinessException("Cost center ID is required.");
        }
        MonthlyBudget budget = monthlyBudgetRepository.findById(dto.monthlyBudget().id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Monthly budget not found. ID = " + dto.monthlyBudget().id()));
        CostCenter costCenter = findCostCenterById(dto.costCenter().id());
        validateDueDate(dto.dueDate(), budget);
        Expense expense = expenseMapper.toEntity(dto, budget, costCenter);
        return expenseMapper.toResponse(expenseRepository.save(expense));
    }

    /**
     * Updates an existing expense entry.
     * Validates the resulting fields, due date and any replacement cost center before applying the update.
     *
     * @param id the identifier of the expense entry to update
     * @param dto the DTO containing updated information
     * @return a {@link ExpenseDTO.Response} representing the updated expense entry
     * @throws ResourceNotFoundException if the expense entry does not exist or the replacement cost center is not found
     * @throws BusinessException if the resulting fields or due date violate the expense constraints
     */
    @Transactional
    public ExpenseDTO.Response updateExpense(Long id, ExpenseDTO.Update dto) {
        Expense expense = findEntityById(id);
        validateFields(dto.description().orElse(expense.getDescription()),
                dto.amount().orElse(expense.getAmount()), dto.comment().orElse(expense.getComment()));
        Optional<CostCenter> costCenter = dto.costCenter().map(ref -> findCostCenterById(ref.id()));
        validateDueDate(dto.dueDate().orElse(expense.getDueDate()), expense.getMonthlyBudget());
        expenseMapper.updateEntity(expense, dto, costCenter);
        return expenseMapper.toResponse(expenseRepository.save(expense));
    }

    /**
     * Deletes an expense entry from the system.
     *
     * @param id the identifier of the expense entry to delete
     * @throws ResourceNotFoundException if the expense entry does not exist
     */
    @Transactional
    public void deleteExpense(Long id) {
        expenseRepository.delete(findEntityById(id));
    }

    private Expense findEntityById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found. ID = " + id));
    }

    private CostCenter findCostCenterById(Long id) {
        if (id == null) {
            throw new BusinessException("Cost center ID is required.");
        }
        return costCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cost center not found. ID = " + id));
    }

    private void validateDueDate(LocalDate date, MonthlyBudget budget) {
        if (date == null || !YearMonth.from(date).equals(budget.getReferenceMonth())) {
            throw new BusinessException("Date must be between the first and last day of the month.");
        }
    }

    /**
     * Validates the existing entity constraints explicitly because no Bean Validation
     * provider is configured.
     */
    private void validateFields(String description, BigDecimal amount, String comment) {
        if (description == null || description.isBlank() || description.length() > 50) {
            throw new BusinessException("Description is required and must not exceed 50 characters.");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new BusinessException("Amount must be greater than zero.");
        }
        if (comment != null && comment.length() > 200) {
            throw new BusinessException("Comment must not exceed 200 characters.");
        }
    }
}
