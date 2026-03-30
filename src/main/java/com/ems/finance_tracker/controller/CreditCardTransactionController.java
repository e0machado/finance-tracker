package com.ems.finance_tracker.controller;

import com.ems.finance_tracker.dto.CreditCardTransactionDTO;
import com.ems.finance_tracker.service.CreditCardTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling {@link com.ems.finance_tracker.model.entity.CreditCardTransaction}
 * related HTTP requests.
 * Provides CRUD operations for credit card transactions.
 *
 * @author Evandro Machado
 */
@RestController
@RequestMapping("/credit-card-transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class CreditCardTransactionController {

    private final CreditCardTransactionService creditCardTransactionService;

    /**
     * Retrieves all credit card transactions.
     *
     * @return HTTP 200 OK with a list of {@link CreditCardTransactionDTO.Response} representing all transactions
     */
    @GetMapping
    public ResponseEntity<List<CreditCardTransactionDTO.Response>> findAll() {
        return ResponseEntity.ok(creditCardTransactionService.findAllCreditCardTransactions());
    }

    /**
     * Retrieves a single credit card transaction by ID.
     *
     * @param id the transaction identifier
     * @return HTTP 200 OK with a {@link CreditCardTransactionDTO.Response} containing transaction data
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the transaction does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditCardTransactionDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(creditCardTransactionService.findCreditCardTransactionById(id));
    }

    /**
     * Creates a new credit card transaction.
     *
     * @param dto the {@link CreditCardTransactionDTO.Request} containing transaction creation data
     * @return HTTP 201 Created with a {@link CreditCardTransactionDTO.Response} representing the persisted transaction
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the credit card, category or statement is not found
     */
    @PostMapping
    public ResponseEntity<CreditCardTransactionDTO.Response> create(@Valid @RequestBody CreditCardTransactionDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creditCardTransactionService.saveCreditCardTransaction(dto));
    }

    /**
     * Updates an existing credit card transaction.
     *
     * @param id the identifier of the transaction to be updated
     * @param dto the {@link CreditCardTransactionDTO.Update} containing updated transaction data
     * @return HTTP 200 OK with a {@link CreditCardTransactionDTO.Response} representing the updated transaction
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the transaction does not exist
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CreditCardTransactionDTO.Response> update(@PathVariable Long id, @Valid @RequestBody CreditCardTransactionDTO.Update dto) {
        return ResponseEntity.ok(creditCardTransactionService.updateCreditCardTransaction(id, dto));
    }

    /**
     * Deletes a credit card transaction by ID.
     *
     * @param id the identifier of the transaction to be deleted
     * @return HTTP 204 No Content if deletion is successful
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the transaction does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        creditCardTransactionService.deleteCreditCardTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
