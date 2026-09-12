package com.ems.financetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Interface encapsulating {@link com.ems.financetracker.model.entity.Income}-related
 * Data Transfer Objects (DTOs).
 */
public interface IncomeDTO {

    /**
     * Reference DTO representing a {@link com.ems.financetracker.model.entity.MonthlyBudget} by its identifier.
     */
    record MonthlyBudgetRef(@NotNull Long id) {}

    /**
     * DTO used for income entry creation requests.
     */
    @Schema(name = "IncomeRequest")
    record Request(
            @NotBlank @Size(max = 50) String description,
            @NotNull @Positive BigDecimal amount,
            @NotNull LocalDate date,
            boolean isFixed,
            @NotNull @Valid MonthlyBudgetRef monthlyBudget
    ) {}

    /**
     * DTO used in API responses, exposing income entry data.
     */
    @Schema(name = "IncomeResponse")
    record Response(
            Long id,
            String description,
            BigDecimal amount,
            LocalDate date,
            boolean isFixed,
            MonthlyBudgetRef monthlyBudget
    ) {}

    /**
     * DTO used for partially updating income information within its assigned budget.
     */
    @Schema(name = "IncomeUpdate")
    record Update(
            Optional<@Size(max = 50) @Pattern(regexp = "(?s).*\\S.*") String> description,
            Optional<@Positive BigDecimal> amount,
            Optional<LocalDate> date,
            Optional<Boolean> isFixed
    ) {
        public Update {
            description = description == null ? Optional.empty() : description;
            amount = amount == null ? Optional.empty() : amount;
            date = date == null ? Optional.empty() : date;
            isFixed = isFixed == null ? Optional.empty() : isFixed;
        }
    }
}
