package com.ems.financetracker.dto;

import com.ems.financetracker.model.enums.ExpenseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Interface encapsulating {@link com.ems.financetracker.model.entity.Expense}-related
 * Data Transfer Objects (DTOs).
 */
public interface ExpenseDTO {

    /**
     * Reference DTO representing a {@link com.ems.financetracker.model.entity.MonthlyBudget} by its identifier.
     */
    record MonthlyBudgetRef(@NotNull Long id) {}
    /**
     * Reference DTO representing a {@link com.ems.financetracker.model.entity.CostCenter} by its identifier.
     */
    record CostCenterRef(@NotNull Long id) {}

    /**
     * DTO used for expense entry creation requests.
     */
    @Schema(name = "ExpenseRequest")
    record Request(
            @NotBlank @Size(max = 50) String description,
            @NotNull @Positive BigDecimal amount,
            @NotNull LocalDate dueDate,
            boolean isFixed,
            @Size(max = 200) String comment,
            @NotNull @Valid CostCenterRef costCenter,
            @NotNull @Valid MonthlyBudgetRef monthlyBudget
    ) {}

    /**
     * DTO used in API responses, exposing expense entry data.
     */
    @Schema(name = "ExpenseResponse")
    record Response(
            Long id,
            String description,
            BigDecimal amount,
            LocalDate dueDate,
            boolean isFixed,
            String comment,
            ExpenseStatus status,
            CostCenterRef costCenter,
            MonthlyBudgetRef monthlyBudget
    ) {}

    /**
     * DTO used for partially updating expense information and its cost center.
     *
     * <p>The assigned budget and payment status are not updated through this DTO.</p>
     */
    @Schema(name = "ExpenseUpdate")
    record Update(
            Optional<@Size(max = 50) @Pattern(regexp = "(?s).*\\S.*") String> description,
            Optional<@Positive BigDecimal> amount,
            Optional<LocalDate> dueDate,
            Optional<Boolean> isFixed,
            Optional<@Size(max = 200) String> comment,
            Optional<@Valid CostCenterRef> costCenter
    ) {
        public Update {
            description = description == null ? Optional.empty() : description;
            amount = amount == null ? Optional.empty() : amount;
            dueDate = dueDate == null ? Optional.empty() : dueDate;
            isFixed = isFixed == null ? Optional.empty() : isFixed;
            comment = comment == null ? Optional.empty() : comment;
            costCenter = costCenter == null ? Optional.empty() : costCenter;
        }
    }
}
