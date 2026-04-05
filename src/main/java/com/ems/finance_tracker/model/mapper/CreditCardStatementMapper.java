package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.CreditCardStatementDTO;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.CreditCardStatement;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between
 * {@link CreditCardStatement} entities and {@link CreditCardStatementDTO} records.
 *
 * @author Evandro Machado
 */
@Component
public class CreditCardStatementMapper {

    /**
     * Converts a {@link CreditCardStatementDTO.Request} and its associated {@link CreditCard}
     * into a {@link CreditCardStatement} entity.
     *
     * <p>The {@code closingDay} and {@code dueDay} are inherited from the provided
     * {@link CreditCard} rather than the request payload.</p>
     *
     * @param dto the request DTO containing the statement data
     * @param creditCard the credit card associated with the statement
     * @return a new {@link CreditCardStatement} entity
     */
    public CreditCardStatement toEntity(CreditCardStatementDTO.Request dto,
                                        CreditCard creditCard) {
        return CreditCardStatement.builder()
                .referenceMonth(dto.referenceMonth())
                .closingDay(creditCard.getClosingDay())
                .dueDay(creditCard.getDueDay())
                .creditCard(creditCard)
                .build();
    }

    /**
     * Converts a {@link CreditCardStatement} entity into a {@link CreditCardStatementDTO.Response}.
     *
     * @param statement the entity to convert
     * @return the corresponding response DTO
     */
    public CreditCardStatementDTO.Response toResponse(CreditCardStatement statement) {
        return new CreditCardStatementDTO.Response(
                statement.getId(),
                statement.getReferenceMonth(),
                statement.getClosingDay(),
                statement.getDueDay(),
                new CreditCardStatementDTO.CreditCardRef(
                        statement.getCreditCard().getId(),
                        statement.getCreditCard().getName()
                )
        );
    }

    /**
     * Applies the fields present in {@link CreditCardStatementDTO.Update} to an existing
     * {@link CreditCardStatement} entity.
     *
     * <p>Only fields wrapped in a non-empty {@link java.util.Optional} are updated.</p>
     *
     * @param statement the entity to be updated
     * @param dto the update DTO containing the new values
     */
    public void updateEntity(CreditCardStatement statement, CreditCardStatementDTO.Update dto) {
        dto.closingDay().ifPresent(statement::setClosingDay);
        dto.dueDay().ifPresent(statement::setDueDay);
    }
}
