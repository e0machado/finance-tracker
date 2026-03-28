package com.ems.finance_tracker.dto;

import com.ems.finance_tracker.model.enums.CreditCardTransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Interface encapsulating {@link com.ems.finance_tracker.model.entity.CreditCardTransaction}-related
 * Data Transfer Objects (DTOs).
 *
 * @author Evandro Machado
 */
public interface CreditCardTransactionDTO {

    /**
     * Reference DTO representing a {@link com.ems.finance_tracker.model.entity.CreditCard} by its identifier.
     */
    record CreditCardRef(@NotNull Long id) {}

    /**
     * Reference DTO representing a {@link com.ems.finance_tracker.model.entity.Category} by its identifier.
     */
    record CategoryRef(@NotNull Long id) {}

    /**
     * Reference DTO representing a {@link com.ems.finance_tracker.model.entity.CreditCardStatement} by its identifier.
     */
    record CreditCardStatementRef(@NotNull Long id) {}

    /**
     * DTO used for credit card transaction creation requests.
     */
    @Schema(name = "CreditCardTransactionRequest")
    record Request(
            @NotBlank
            @Size(max = 50)
            String description,

            @NotNull
            CreditCardTransactionType type,

            @NotNull
            @Positive
            BigDecimal amount,

            boolean isInstallment,

            @NotNull
            @Positive
            Integer currentInstallment,

            @NotNull
            @Positive
            Integer totalInstallments,

            @NotNull
            LocalDate purchaseDate,

            @Size(max = 200)
            String comment,

            @NotNull
            CreditCardRef creditCard,

            @NotNull
            CategoryRef category,

            @NotNull
            CreditCardStatementRef statement
    )
    {
        @AssertTrue(message = "For non-installment transactions, currentInstallment and " +
                "totalInstallments must both be equal to 1.")
        public boolean isValidNonInstallment() {
            if (!isInstallment) {
                return currentInstallment == 1 && totalInstallments == 1;
            }
            return true;
        }

        @AssertTrue(message = "For installment transactions, totalInstallments must be greater than 1."
        )
        public boolean isValidTotalInstallment() {
            if (isInstallment) {
                return totalInstallments > 1;
            }
            return true;
        }

        @AssertTrue(message = "For installment transactions, currentInstallment must be " +
                "less than or equal to totalInstallments.")
        public boolean isValidCurrentInstallment() {
            if (isInstallment) {
                return currentInstallment <= totalInstallments;
            }
            return true;
        }
    }

    /**
     * DTO used in API responses representing the credit card transaction data.
     */
    @Schema(name = "CreditCardTransactionResponse")
    record Response(
            Long id,
            String description,
            CreditCardTransactionType type,
            BigDecimal amount,
            boolean isInstallment,
            Integer currentInstallment,
            Integer totalInstallments,
            LocalDate purchaseDate,
            String comment,
            CreditCardRef creditCard,
            CategoryRef category,
            CreditCardStatementRef statement
    ) {}

    /**
     * DTO used for updating credit card transaction information.
     */
    @Schema(name = "CreditCardTransactionUpdate")
    record Update(
            @Size(max = 50)
            Optional<@NotBlank String> description,

            Optional<CreditCardTransactionType> type,

            Optional<@Positive BigDecimal> amount,

            Optional<Boolean> isInstallment,

            Optional<@Positive Integer> currentInstallment,

            Optional<@Positive Integer> totalInstallments,

            Optional<LocalDate> purchaseDate,

            @Size(max = 200)
            Optional<String> comment,

            Optional<CategoryRef> category
    )
    {
        @AssertTrue(message = "For updates, currentInstallment and totalInstallments can only " +
                "be updated when isInstallment is explicitly provided.")
        public boolean isInstallmentUpdate() {
            if (isInstallment.isEmpty()) {
                return currentInstallment.isEmpty() && totalInstallments.isEmpty();
            }
            return true;
        }

        @AssertTrue(message = "For non-installment updates, currentInstallment and " +
                "totalInstallments must both be 1.")
        public boolean isValidNonInstallmentUpdate() {
            if (!isInstallment.orElse(false)) {
                return currentInstallment.orElse(1) == 1
                        && totalInstallments.orElse(1) == 1;
            }
            return true;
        }

        @AssertTrue(message = "For installment updates, totalInstallments must be " +
                "greater than 1 and currentInstallment must not exceed totalInstallments.")
        public boolean isValidInstallmentUpdate() {
            if (isInstallment.orElse(false)) {
                int current = currentInstallment.orElse(1);
                int total = totalInstallments.orElse(1);

                return total > 1 && current <= total;
            }
            return true;
        }
    }

}
