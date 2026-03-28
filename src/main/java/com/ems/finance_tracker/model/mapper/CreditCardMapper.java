package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.CreditCardDTO;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting {@link CreditCardDTO} to {@link CreditCard} entities and vice versa.
 *
 * @author Evandro Machado
 */
@Component
public class CreditCardMapper {

    /**
     * Converts a Credit Card creation request DTO into a Credit Card entity.
     * Note: The user association must be handled at the service level.
     *
     * @param dto the credit card creation request data
     * @param user the user associated with the credit card data
     * @return a new credit card entity ready for persistence
     */
    public CreditCard toEntity(CreditCardDTO.Request dto, User user) {
        return CreditCard.of(
                        dto.name(),
                        dto.creditLimit(),
                        dto.closingDay(),
                        dto.dueDay(),
                        dto.availableLimit(),
                        user
                );

    }

    /**
     * Converts a Credit Card entity into a response DTO.
     *
     * @param creditCard the persisted credit card entity
     * @return a response DTO
     */
    public CreditCardDTO.Response toResponse(CreditCard creditCard) {
        return new CreditCardDTO.Response(
                creditCard.getId(),
                creditCard.getName(),
                creditCard.getCreditLimit(),
                creditCard.getClosingDay(),
                creditCard.getDueDay(),
                creditCard.getAvailableLimit(),
                new CreditCardDTO.UserRef(
                        creditCard.getUser().getId(),
                        creditCard.getUser().getName()
                )
        );
    }

    /**
     * Updates mutable fields of an existing Credit Card entity
     * using data from an update DTO.
     *
     * @param creditCard the existing credit card entity to be updated
     * @param dto the DTO containing updated profile information
     */
    public void updateEntity(CreditCard creditCard, CreditCardDTO.Update dto) {
        dto.name().ifPresent(creditCard::setName);
        dto.creditLimit().ifPresent(creditCard::updateCreditLimit);
    }
}
