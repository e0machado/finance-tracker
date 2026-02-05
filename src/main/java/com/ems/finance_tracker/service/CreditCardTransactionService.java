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

@Service
@RequiredArgsConstructor
public class CreditCardTransactionService {

    private final CreditCardTransactionRepository creditCardTransactionRepository;
    private final CreditCardTransactionMapper creditCardTransactionMapper;
    private final CreditCardRepository creditCardRepository;
    private final CategoryRepository categoryRepository;
    private final CreditCardStatementRepository creditCardStatementRepository;

    public List<CreditCardTransactionDTO.Response> findAllCreditCardTransactions() {
        return creditCardTransactionRepository.findAll()
                .stream()
                .map(creditCardTransactionMapper::toResponse)
                .toList();
    }

    public CreditCardTransactionDTO.Response findCreditCardTransactionById(Long id) {
        CreditCardTransaction transaction = findEntityById(id);

        return creditCardTransactionMapper.toResponse(transaction);
    }

    @Transactional
    public CreditCardTransactionDTO.Response saveCreditCardTransaction(CreditCardTransactionDTO.Request dto) {
        CreditCard creditCard = creditCardRepository.findById(dto.creditCard().id())
                .orElseThrow(() -> new ResourceNotFoundException("Credit card not found. ID = " + dto.creditCard().id()));
        Category category = categoryRepository.findById(dto.category().id())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found. ID = " + dto.category().id()));
        CreditCardStatement creditCardStatement = creditCardStatementRepository.findById(dto.statement().id())
                .orElseThrow(() -> new ResourceNotFoundException("Statement not found. ID = " + dto.statement().id()));

        CreditCardTransaction transaction = creditCardTransactionMapper.toEntity(dto, creditCard, category, creditCardStatement);

        return creditCardTransactionMapper.toResponse(creditCardTransactionRepository.save(transaction));
    }

    @Transactional
    public CreditCardTransactionDTO.Response updateCreditCardTransaction(Long id, CreditCardTransactionDTO.Update dto) {
        CreditCardTransaction existingTransaction = findEntityById(id);
        Category category = null;

        if (dto.category().isPresent()) {
            Long categoryId = dto.category().get().id();
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found. ID = " + categoryId));
        }

        creditCardTransactionMapper.updateEntity(existingTransaction, dto, category);

        return creditCardTransactionMapper.toResponse(creditCardTransactionRepository.save(existingTransaction));
    }

    @Transactional
    public void deleteCreditCardTransaction(Long id) {
        CreditCardTransaction transaction = findEntityById(id);
        creditCardTransactionRepository.delete(transaction);
    }

    private CreditCardTransaction findEntityById(Long id) {
        return creditCardTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit card transaction not found. ID = " + id));
    }
}
