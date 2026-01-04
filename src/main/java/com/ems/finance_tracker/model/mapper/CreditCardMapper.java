package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.CreditCardDTO;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting {@link CreditCardDTO} to {@link CreditCard} entities and vice versa.
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
        return CreditCard.builder()
                .name(dto.name())
                .creditLimit(dto.creditLimit())
                .closingDay(dto.closingDay())
                .dueDay(dto.dueDay())
                .currentBalance(dto.currentBalance())
                .user(user)
                .build();
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
                creditCard.getCurrentBalance(),
                creditCard.getUser().getId()
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
        creditCard.setName(dto.name());
        creditCard.setCreditLimit(dto.creditLimit());
        creditCard.setClosingDay(dto.closingDay());
        creditCard.setDueDay(dto.dueDay());
    }
}
