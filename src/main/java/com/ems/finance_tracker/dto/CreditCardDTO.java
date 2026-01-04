package com.ems.finance_tracker.dto;

import com.ems.finance_tracker.model.entity.CreditCard;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Interface for {@link CreditCard}- related Data Transfer Objects (DTOs).
 */
public interface CreditCardDTO {

    /**
     * DTO used for credit card creation requests.
     */
    @Schema(name = "CreditCardRequest")
    record Request(
            @NotBlank
            @Size(max = 50)
            String name,

            @NotNull
            @PositiveOrZero
            BigDecimal creditLimit,

            @NotNull
            @Min(1)
            @Max(31)
            Integer closingDay,

            @NotNull
            @Min(1)
            @Max(31)
            Integer dueDay,

            @NotNull
            @PositiveOrZero
            BigDecimal currentBalance,

            @NotNull
            Long userId
    ) {}

    /**
     * DTO used in API responses representing the credit card data.
     */
    @Schema(name = "CreditCardResponse")
    record Response(
            Long id,
            String name,
            BigDecimal creditLimit,
            Integer closingDay,
            Integer dueDay,
            BigDecimal currentBalance,
            Long userId
    ) {}

    /**
     * DTO used for updating basic credit card information,
     * excluding current balance and owner's user.
     */
    @Schema(name = "CreditCardUpdate")
    record Update(
            @NotBlank
            @Size(max = 50)
            String name,

            @NotNull
            @PositiveOrZero
            BigDecimal creditLimit,

            @NotNull
            @Min(1)
            @Max(31)
            Integer closingDay,

            @NotNull
            @Min(1)
            @Max(31)
            Integer dueDay
    ) {}

}
