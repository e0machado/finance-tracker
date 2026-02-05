package com.ems.finance_tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Optional;

public interface CreditCardStatementDTO {

    record CreditCardRef(
            @NotNull
            Long id,

            @NotBlank
            @Size(max = 50)
            String name

    ) {}

    @Schema(name = "CreditCardStatementRequest")
    record Request(
            @NotBlank
            String referenceMonth,

            @NotNull
            CreditCardRef creditCard
    ) {}

    @Schema(name = "CreditCardStatementResponse")
    record Response(
            Long id,
            String referenceMonth,
            Integer closingDay,
            Integer dueDay,
            CreditCardRef creditCard
    ) {}

    @Schema(name = "CreditCardStatementUpdate")
    record Update(
            Optional<@NotNull @Min(1) @Max(31) Integer> closingDay,

            Optional<@NotNull @Min(1) @Max(31) Integer> dueDay
    ) {}

}
