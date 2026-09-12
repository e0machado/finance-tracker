package com.ems.financetracker.service;

import com.ems.financetracker.dto.IncomeDTO;
import com.ems.financetracker.exception.BusinessException;
import com.ems.financetracker.exception.ResourceNotFoundException;
import com.ems.financetracker.model.entity.Income;
import com.ems.financetracker.model.entity.MonthlyBudget;
import com.ems.financetracker.model.mapper.IncomeMapper;
import com.ems.financetracker.repository.IncomeRepository;
import com.ems.financetracker.repository.MonthlyBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Service responsible for managing {@link Income} business operations.
 * Handles validation, persistence coordination and DTO/entity transformations.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final IncomeMapper incomeMapper;

    /**
     * Retrieves all income entries from the system.
     *
     * @return a list of {@link IncomeDTO.Response} representing all registered income entries
     */
    public List<IncomeDTO.Response> findAllIncomes() {
        return incomeRepository.findAll().stream().map(incomeMapper::toResponse).toList();
    }

    /**
     * Retrieves a single income entry by its identifier.
     *
     * @param id the income entry identifier
     * @return a {@link IncomeDTO.Response} with the income entry data
     * @throws ResourceNotFoundException if the income entry does not exist
     */
    public IncomeDTO.Response findIncomeById(Long id) {
        return incomeMapper.toResponse(findEntityById(id));
    }

    /**
     * Creates and persists a new income entry.
     * Resolves the budget and validates that the income date falls within its reference month.
     *
     * @param dto the income entry creation request data
     * @return a {@link IncomeDTO.Response} representing the persisted income entry
     * @throws BusinessException if required data is invalid or the date is outside the budget month
     * @throws ResourceNotFoundException if an associated resource does not exist
     */
    @Transactional
    public IncomeDTO.Response saveIncome(IncomeDTO.Request dto) {
        validateFields(dto.description(), dto.amount());
        if (dto.monthlyBudget() == null || dto.monthlyBudget().id() == null) {
            throw new BusinessException("Monthly budget ID is required.");
        }
        MonthlyBudget budget = monthlyBudgetRepository.findById(dto.monthlyBudget().id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Monthly budget not found. ID = " + dto.monthlyBudget().id()));
        validateDate(dto.date(), budget);
        Income income = incomeMapper.toEntity(dto, budget);
        return incomeMapper.toResponse(incomeRepository.save(income));
    }

    /**
     * Updates an existing income entry.
     * Validates the resulting fields and date before applying the update.
     *
     * @param id the identifier of the income entry to update
     * @param dto the DTO containing updated information
     * @return a {@link IncomeDTO.Response} representing the updated income entry
     * @throws ResourceNotFoundException if the income entry does not exist
     * @throws BusinessException if the resulting fields or date violate the income constraints
     */
    @Transactional
    public IncomeDTO.Response updateIncome(Long id, IncomeDTO.Update dto) {
        Income income = findEntityById(id);
        validateFields(dto.description().orElse(income.getDescription()), dto.amount().orElse(income.getAmount()));
        validateDate(dto.date().orElse(income.getDate()), income.getMonthlyBudget());
        incomeMapper.updateEntity(income, dto);
        return incomeMapper.toResponse(incomeRepository.save(income));
    }

    /**
     * Deletes an income entry from the system.
     *
     * @param id the identifier of the income entry to delete
     * @throws ResourceNotFoundException if the income entry does not exist
     */
    @Transactional
    public void deleteIncome(Long id) {
        incomeRepository.delete(findEntityById(id));
    }

    private Income findEntityById(Long id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found. ID = " + id));
    }

    private void validateDate(LocalDate date, MonthlyBudget budget) {
        if (date == null || !YearMonth.from(date).equals(budget.getReferenceMonth())) {
            throw new BusinessException("Date must be between the first and last day of the month.");
        }
    }

    /**
     * Validates the existing entity constraints explicitly because no Bean Validation
     * provider is configured.
     */
    private void validateFields(String description, BigDecimal amount) {
        if (description == null || description.isBlank() || description.length() > 50) {
            throw new BusinessException("Description is required and must not exceed 50 characters.");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new BusinessException("Amount must be greater than zero.");
        }
    }
}
