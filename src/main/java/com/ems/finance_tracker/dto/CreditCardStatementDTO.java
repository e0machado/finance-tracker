package com.ems.finance_tracker.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.YearMonth;
import java.util.Objects;
import java.util.Optional;

/**
 * Interface encapsulating {@link com.ems.finance_tracker.model.entity.CreditCardStatement}-related
 * Data Transfer Objects (DTOs).
 *
 * @author Evandro Machado
 */
public interface CreditCardStatementDTO {

    /**
     * Reference DTO representing a {@link com.ems.finance_tracker.model.entity.CreditCard}
     * by its identifier and name.
     */
    record CreditCardRef(
            @NotNull
            Long id,

            String name
    ) {}

    /**
     * DTO used for credit card statement creation requests.
     */
    @Schema(name = "CreditCardStatementRequest")
    record Request(

            @JsonSerialize(using = YearMonthSerializer.class)
            @JsonDeserialize(using = YearMonthDeserializer.class)
            @Schema(type = "string", example = "2025-01", pattern = "^\\d{4}-(0[1-9]|1[0-2])$")
            @NotNull
            YearMonth referenceMonth,

            @NotNull
            CreditCardRef creditCard
    ) {}

    /**
     * DTO used in API responses representing the credit card statement data.
     */
    @Schema(name = "CreditCardStatementResponse")
    record Response(
            Long id,
            @JsonSerialize(using = YearMonthSerializer.class)
            @JsonDeserialize(using = YearMonthDeserializer.class)
            @Schema(type = "string", example = "2025-01")
            YearMonth referenceMonth,
            Integer closingDay,
            Integer dueDay,
            CreditCardRef creditCard
    ) {}

    /**
     * DTO used for partially updating credit card statement billing dates.
     */
    @Schema(name = "CreditCardStatementUpdate")
    record Update(
            Optional<@NotNull @Min(1) @Max(31) Integer> closingDay,

            Optional<@NotNull @Min(1) @Max(31) Integer> dueDay
    ) {
        @AssertTrue(message = "Closing day and due day must not be equal.")
        public boolean isClosingDayDifferentFromDueDayOnUpdate() {
            if (closingDay.isEmpty() || dueDay.isEmpty()) {
                return true;
            }

            return !Objects.equals(closingDay.get(), dueDay.get());
        }
    }

}
