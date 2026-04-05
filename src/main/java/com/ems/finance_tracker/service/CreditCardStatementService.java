package com.ems.finance_tracker.service;

import com.ems.finance_tracker.dto.CreditCardStatementDTO;
import com.ems.finance_tracker.exception.ResourceNotFoundException;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.CreditCardStatement;
import com.ems.finance_tracker.model.mapper.CreditCardStatementMapper;
import com.ems.finance_tracker.repository.CreditCardRepository;
import com.ems.finance_tracker.repository.CreditCardStatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for managing {@link CreditCardStatement} business operations.
 * Handles validation, persistence coordination and DTO/entity transformations.
 *
 * @author Evandro Machado
 */
@Service
@RequiredArgsConstructor
public class CreditCardStatementService {

    private final CreditCardStatementRepository creditCardStatementRepository;
    private final CreditCardStatementMapper creditCardStatementMapper;
    private final CreditCardRepository creditCardRepository;

    /**
     * Retrieves all credit card statements from the system.
     *
     * // TODO: Replace findAll() with a user-scoped query once Spring Security is fully configured.
     * // Each user should only be able to retrieve their own statements.
     *
     * @return a list of {@link CreditCardStatementDTO.Response} representing all registered statements
     */
    public List<CreditCardStatementDTO.Response> findAllCreditCardStatements() {
        return creditCardStatementRepository.findAll()
                .stream()
                .map(creditCardStatementMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a single credit card statement by its identifier.
     *
     * @param id the statement identifier
     * @return a {@link CreditCardStatementDTO.Response} with the statement data
     * @throws ResourceNotFoundException if the statement does not exist
     */
    public CreditCardStatementDTO.Response findCreditCardStatementById(Long id) {
        CreditCardStatement statement = findEntityById(id);

        return creditCardStatementMapper.toResponse(statement);
    }

    /**
     * Creates and persists a new credit card statement.
     * Associates the statement with the corresponding credit card.
     *
     * @param dto the statement creation request data
     * @return a {@link CreditCardStatementDTO.Response} representing the persisted statement
     * @throws ResourceNotFoundException if the credit card is not found
     */
    @Transactional
    public CreditCardStatementDTO.Response saveCreditCardStatement(CreditCardStatementDTO.Request dto) {
        CreditCard creditCard = creditCardRepository.findById(dto.creditCard().id())
                .orElseThrow(() -> new ResourceNotFoundException("Credit card not found. ID = " + dto.creditCard().id()));

        CreditCardStatement statement = creditCardStatementMapper.toEntity(dto, creditCard);

        return creditCardStatementMapper.toResponse(creditCardStatementRepository.save(statement));
    }

    /**
     * Updates an existing credit card statement's billing cycle information.
     *
     * @param id the identifier of the statement to be updated
     * @param dto the DTO containing updated statement data
     * @return a {@link CreditCardStatementDTO.Response} representing the updated statement
     * @throws ResourceNotFoundException if the statement does not exist
     */
    @Transactional
    public CreditCardStatementDTO.Response updateCreditCardStatement(Long id, CreditCardStatementDTO.Update dto) {
        CreditCardStatement existingStatement = findEntityById(id);

        creditCardStatementMapper.updateEntity(existingStatement, dto);

        return creditCardStatementMapper.toResponse(creditCardStatementRepository.save(existingStatement));
    }

    /**
     * Deletes a credit card statement from the system.
     *
     * @param id the identifier of the statement to be deleted
     * @throws ResourceNotFoundException if the statement does not exist
     */
    @Transactional
    public void deleteCreditCardStatement(Long id) {
        CreditCardStatement statement = findEntityById(id);
        creditCardStatementRepository.delete(statement);
    }

    /**
     * Retrieves a CreditCardStatement entity by its identifier.
     *
     * @param id the statement identifier
     * @return the CreditCardStatement entity
     * @throws ResourceNotFoundException if the statement does not exist
     */
    private CreditCardStatement findEntityById(Long id) {
        return creditCardStatementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit card statement not found. ID = " + id));
    }
}
