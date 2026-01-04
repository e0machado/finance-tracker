package com.ems.finance_tracker.service;

import com.ems.finance_tracker.dto.CreditCardDTO;
import com.ems.finance_tracker.exception.ResourceNotFoundException;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.User;
import com.ems.finance_tracker.model.mapper.CreditCardMapper;
import com.ems.finance_tracker.repository.CreditCardRepository;
import com.ems.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for managing {@link CreditCard}- related business operations.
 * Handles validation, persistence coordination and DTO/entity transformations.
 */
@Service
@RequiredArgsConstructor
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;
    private final UserRepository userRepository;

    /**
     * Retrieves all credit cards from the system.
     *
     * @return a list of {@link CreditCardDTO.Response} representing all
     * registered credit cards
     */
    public List<CreditCardDTO.Response> findAllCreditCards() {
        return creditCardRepository.findAll()
                .stream()
                .map(creditCardMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a single credit card by its identifier.
     *
     * @param id the credit card identifier
     * @return a {@link CreditCardDTO.Response} with the credit card data
     * @throws ResourceNotFoundException if the credit card does not exist
     */
    public CreditCardDTO.Response findCreditCardById(Long id) {
        CreditCard creditCard = findEntityById(id);

        return creditCardMapper.toResponse(creditCard);
    }

    /**
     * Creates and persists a new credit card.
     * Associates the owner user with their credit card.
     *
     * @param dto the credit card creation request data
     * @return a {@link CreditCardDTO.Response} representing the persisted credit card
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public CreditCardDTO.Response saveCreditCard(CreditCardDTO.Request dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found. ID = " + dto.userId()));

        CreditCard creditCard = creditCardMapper.toEntity(dto, user);

        return creditCardMapper.toResponse(creditCardRepository.save(creditCard));
    }

    /**
     * Updates an existing credit card's details.
     *
     * @param id the identifier of the credit card to be updated
     * @param dto the DTO containing updated card settings
     * @return a {@link CreditCardDTO.Response} representing the updated credit card
     * @throws ResourceNotFoundException if the credit card does not exist
     */
    @Transactional
    public CreditCardDTO.Response updateCreditCard(Long id, CreditCardDTO.Update dto) {
        CreditCard existingCreditCard = findEntityById(id);

        creditCardMapper.updateEntity(existingCreditCard, dto);

        return creditCardMapper.toResponse(creditCardRepository.save(existingCreditCard));
    }

    /**
     * Deletes a credit card from the system.
     *
     * @param id the identifier of the credit card to be deleted
     * @throws ResourceNotFoundException if the credit card does not exist
     */
    @Transactional
    public void deleteCreditCard(Long id) {
        CreditCard creditCard = findEntityById(id);
        creditCardRepository.delete(creditCard);
    }

    /**
     * Retrieves a Credit Card entity by its identifier.
     *
     * @param id the credit card identifier
     * @return the Credit Card entity
     * @throws ResourceNotFoundException if the credit card does not exist
     */
    private CreditCard findEntityById(Long id) {
        return creditCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit Card not found. ID = " + id));
    }

}
