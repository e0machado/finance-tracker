package com.ems.finance_tracker.controller;

import com.ems.finance_tracker.dto.CreditCardStatementDTO;
import com.ems.finance_tracker.service.CreditCardStatementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling {@link com.ems.finance_tracker.model.entity.CreditCardStatement}
 * related HTTP requests.
 * Provides CRUD operations for credit card statements.
 *
 * @author Evandro Machado
 */
@RestController
@RequestMapping("/credit-card-statements")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class CreditCardStatementController {

    private final CreditCardStatementService creditCardStatementService;

    /**
     * Retrieves all credit card statements.
     *
     * @return HTTP 200 OK with a list of {@link CreditCardStatementDTO.Response} representing all statements
     */
    @GetMapping
    public ResponseEntity<List<CreditCardStatementDTO.Response>> findAll() {
        return ResponseEntity.ok(creditCardStatementService.findAllCreditCardStatements());
    }

    /**
     * Retrieves a single credit card statement by ID.
     *
     * @param id the statement identifier
     * @return HTTP 200 OK with a {@link CreditCardStatementDTO.Response} containing statement data
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the statement does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditCardStatementDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(creditCardStatementService.findCreditCardStatementById(id));
    }

    /**
     * Creates a new credit card statement.
     *
     * @param dto the {@link CreditCardStatementDTO.Request} containing statement creation data
     * @return HTTP 201 Created with a {@link CreditCardStatementDTO.Response} representing the persisted statement
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the associated credit card is not found
     */
    @PostMapping
    public ResponseEntity<CreditCardStatementDTO.Response> create(@Valid @RequestBody CreditCardStatementDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creditCardStatementService.saveCreditCardStatement(dto));
    }

    /**
     * Updates an existing credit card statement's billing cycle information.
     *
     * @param id the identifier of the statement to be updated
     * @param dto the {@link CreditCardStatementDTO.Update} containing updated statement data
     * @return HTTP 200 OK with a {@link CreditCardStatementDTO.Response} representing the updated statement
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the statement does not exist
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CreditCardStatementDTO.Response> update(@PathVariable Long id, @Valid @RequestBody CreditCardStatementDTO.Update dto) {
        return ResponseEntity.ok(creditCardStatementService.updateCreditCardStatement(id, dto));
    }

    /**
     * Deletes a credit card statement by ID.
     *
     * @param id the identifier of the statement to be deleted
     * @return HTTP 204 No Content if deletion is successful
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the statement does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        creditCardStatementService.deleteCreditCardStatement(id);
        return ResponseEntity.noContent().build();
    }

}
