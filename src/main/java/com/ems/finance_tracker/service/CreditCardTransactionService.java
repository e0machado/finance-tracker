package com.ems.finance_tracker.service;

import com.ems.finance_tracker.dto.CreditCardTransactionDTO;
import com.ems.finance_tracker.exception.ResourceNotFoundException;
import com.ems.finance_tracker.model.entity.Category;
import com.ems.finance_tracker.model.entity.CreditCard;
import com.ems.finance_tracker.model.entity.CreditCardStatement;
import com.ems.finance_tracker.model.entity.CreditCardTransaction;
import com.ems.finance_tracker.model.mapper.CreditCardTransactionMapper;
import com.ems.finance_tracker.repository.CategoryRepository;
import com.ems.finance_tracker.repository.CreditCardRepository;
import com.ems.finance_tracker.repository.CreditCardStatementRepository;
import com.ems.finance_tracker.repository.CreditCardTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing {@link CreditCardTransaction} business operations.
 * Handles validation, persistence coordination and DTO/entity transformations.
 *
 * @author Evandro Machado
 */
@Service
@RequiredArgsConstructor
public class CreditCardTransactionService {

    private final CreditCardTransactionRepository creditCardTransactionRepository;
    private final CreditCardTransactionMapper creditCardTransactionMapper;
    private final CreditCardRepository creditCardRepository;
    private final CategoryRepository categoryRepository;
    private final CreditCardStatementRepository creditCardStatementRepository;

    /**
     * Retrieves all credit card transactions from the system.
     *
     * @return a list of {@link CreditCardTransactionDTO.Response} representing all registered transactions
     */
    public List<CreditCardTransactionDTO.Response> findAllCreditCardTransactions() {

        // TODO: Replace findAll() with a user-scoped query once Spring Security is fully configured.
        // Each user should only be able to retrieve their own transactions.

        return creditCardTransactionRepository.findAll()
                .stream()
                .map(creditCardTransactionMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a single credit card transaction by its identifier.
     *
     * @param id the transaction identifier
     * @return a {@link CreditCardTransactionDTO.Response} with the transaction data
     * @throws ResourceNotFoundException if the transaction does not exist
     */
    public CreditCardTransactionDTO.Response findCreditCardTransactionById(Long id) {
        CreditCardTransaction transaction = findEntityById(id);

        return creditCardTransactionMapper.toResponse(transaction);
    }

    /**
     * Creates and persists a new credit card transaction.
     * Applies the financial impact to the associated credit card and registers
     * the transaction in the corresponding statement.
     *
     * @param dto the transaction creation request data
     * @return a {@link CreditCardTransactionDTO.Response} representing the persisted transaction
     * @throws ResourceNotFoundException if the credit card, category or statement is not found
     */
    @Transactional
    public CreditCardTransactionDTO.Response saveCreditCardTransaction(CreditCardTransactionDTO.Request dto) {
        CreditCard creditCard = creditCardRepository.findById(dto.creditCard().id())
                .orElseThrow(() -> new ResourceNotFoundException("Credit card not found. ID = " + dto.creditCard().id()));
        Category category = categoryRepository.findById(dto.category().id())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found. ID = " + dto.category().id()));
        CreditCardStatement creditCardStatement = creditCardStatementRepository.findById(dto.statement().id())
                .orElseThrow(() -> new ResourceNotFoundException("Statement not found. ID = " + dto.statement().id()));

        CreditCardTransaction transaction = creditCardTransactionMapper.toEntity(dto, creditCard, category, creditCardStatement);

        transaction.applyImpact();
        creditCardStatement.addTransaction(transaction);

        return creditCardTransactionMapper.toResponse(creditCardTransactionRepository.save(transaction));
    }

    /**
     * Updates an existing credit card transaction.
     * Reverts the previous financial impact, applies the updated data,
     * and recalculates the impact on the associated credit card.
     *
     * @param id the identifier of the transaction to be updated
     * @param dto the DTO containing updated transaction data
     * @return a {@link CreditCardTransactionDTO.Response} representing the updated transaction
     * @throws ResourceNotFoundException if the transaction or category is not found
     */
    @Transactional
    public CreditCardTransactionDTO.Response updateCreditCardTransaction(Long id, CreditCardTransactionDTO.Update dto) {
        CreditCardTransaction existingTransaction = findEntityById(id);

        Optional<Category> category = dto.category()
                .map(ref -> categoryRepository.findById(ref.id())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found. ID = " + ref.id())));

        existingTransaction.revertImpact();
        creditCardTransactionMapper.updateEntity(existingTransaction, dto, category);
        existingTransaction.applyImpact();

        return creditCardTransactionMapper.toResponse(creditCardTransactionRepository.save(existingTransaction));
    }

    /**
     * Deletes a credit card transaction.
     * Reverts the financial impact on the associated credit card and removes
     * the transaction from its statement before deletion.
     *
     * @param id the identifier of the transaction to be deleted
     * @throws ResourceNotFoundException if the transaction does not exist
     */
    @Transactional
    public void deleteCreditCardTransaction(Long id) {
        CreditCardTransaction transaction = findEntityById(id);

        transaction.revertImpact();
        transaction.getCreditCardStatement().removeTransaction(transaction);

        creditCardTransactionRepository.delete(transaction);
    }

    /**
     * Retrieves a CreditCardTransaction entity by its identifier.
     *
     * @param id the transaction identifier
     * @return the CreditCardTransaction entity
     * @throws ResourceNotFoundException if the transaction does not exist
     */
    private CreditCardTransaction findEntityById(Long id) {
        return creditCardTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit card transaction not found. ID = " + id));
    }
}
