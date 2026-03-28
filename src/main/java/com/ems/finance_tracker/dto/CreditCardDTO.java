package com.ems.finance_tracker.dto;

import com.ems.finance_tracker.model.entity.CreditCard;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Interface for {@link CreditCard}-related Data Transfer Objects (DTOs).
 *
 * @author Evandro Machado
 */
public interface CreditCardDTO {

    /**
     * Reference DTO representing a {@link com.ems.finance_tracker.model.entity.User} by its identifier.
     */
    record UserRef(
            @NotNull
            Long id,

            @NotBlank
            String name
    ) {}

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

            @PositiveOrZero
            BigDecimal availableLimit,

            @NotNull
            UserRef user
    ) {
        @AssertTrue(message = "Closing day and due day must not be equal.")
        public boolean isClosingDayDifferentFromDueDay() {
            if (closingDay == null || dueDay == null) {
                return true;
            }

            return !Objects.equals(closingDay, dueDay);
        }

        @AssertTrue(message = "Available limit must not be greater than credit limit.")
        public boolean isAvailableLimitSmallerThanCreditLimit() {
            if (availableLimit == null || creditLimit == null) {
                return true;
            }

            return availableLimit.compareTo(creditLimit) <= 0;
        }
    }

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
            BigDecimal availableLimit,
            UserRef user
    ) {}

    /**
     * DTO used for updating basic credit card information,
     * excluding available limit and owner's user.
     */
    @Schema(name = "CreditCardUpdate")
    record Update(
            @Size(max = 50)
            Optional<@NotBlank String> name,

            Optional<@NotNull @PositiveOrZero BigDecimal> creditLimit,

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
