package com.ems.financetracker.model.mapper;

import com.ems.financetracker.dto.IncomeDTO;
import com.ems.financetracker.model.entity.Income;
import com.ems.financetracker.model.entity.MonthlyBudget;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting {@link IncomeDTO} to {@link Income}
 * and vice versa.
 */
@Component
public class IncomeMapper {

    /**
     * Converts an income entry creation request into an entity.
     *
     * @param dto the income entry creation request data
     * @param budget the associated monthly budget
     * @return a new income entry entity ready for persistence
     */
    public Income toEntity(IncomeDTO.Request dto, MonthlyBudget budget) {
        Income income = new Income();
        income.setDescription(dto.description());
        income.setAmount(dto.amount());
        income.setDate(dto.date());
        income.setFixed(dto.isFixed());
        income.setMonthlyBudget(budget);
        return income;
    }

    /**
     * Converts an income entry entity into a response DTO.
     *
     * @param income the persisted income entry
     * @return a response DTO exposing income entry data
     */
    public IncomeDTO.Response toResponse(Income income) {
        return new IncomeDTO.Response(
                income.getId(), income.getDescription(), income.getAmount(),
                income.getDate(), income.isFixed(),
                new IncomeDTO.MonthlyBudgetRef(income.getMonthlyBudget().getId())
        );
    }

    /**
     * Applies the fields present in the update DTO to the existing income entry.
     *
     * @param income the existing income entry entity
     * @param dto the DTO containing updated information
     */
    public void updateEntity(Income income, IncomeDTO.Update dto) {
        dto.description().ifPresent(income::setDescription);
        dto.amount().ifPresent(income::setAmount);
        dto.date().ifPresent(income::setDate);
        dto.isFixed().ifPresent(income::setFixed);
    }
}
