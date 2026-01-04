package com.ems.finance_tracker.controller;

import com.ems.finance_tracker.dto.CreditCardDTO;
import com.ems.finance_tracker.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling {@link com.ems.finance_tracker.model.entity.CreditCard}- related HTTP requests.
 * Provides CRUD operations for credit cards.
 */
@RestController
@RequestMapping("/credit-cards")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CreditCardController {

    private final CreditCardService creditCardService;

    /**
     * Retrieves all credit cards.
     * Intended for administrative or internal usage.
     *
     * @return HTTP 200 OK with a list of {@link CreditCardDTO.Response} representing all credit cards
     */
    @GetMapping
    public ResponseEntity<List<CreditCardDTO.Response>> findAllCreditCards() {
        return ResponseEntity.ok(creditCardService.findAllCreditCards());
    }

    /**
     * Retrieves a single credit card by ID.
     *
     * @param id the credit card identifier
     * @return HTTP 200 OK with a {@link CreditCardDTO.Response} containing credit card data
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the credit card does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditCardDTO.Response> findCreditCardById(@PathVariable Long id) {
        return ResponseEntity.ok(creditCardService.findCreditCardById(id));
    }

    /**
     * Creates a new credit card.
     *
     * @param dto the {@link CreditCardDTO.Request} containing credit card creation data
     * @return HTTP 201 Created with a {@link CreditCardDTO.Response} representing the persisted credit card
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the associated user is not found
     */
    @PostMapping
    public ResponseEntity<CreditCardDTO.Response> createCreditCard(@Valid @RequestBody CreditCardDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creditCardService.saveCreditCard(dto));
    }

    /**
     * Updates an existing credit card's details.
     *
     * @param id the identifier of the credit card to be updated
     * @param dto the {@link CreditCardDTO.Update} containing updated credit card data
     * @return HTTP 200 OK with a {@link CreditCardDTO.Response} representing the updated credit card
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the credit card does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<CreditCardDTO.Response> updateCreditCard(@PathVariable Long id, @Valid @RequestBody CreditCardDTO.Update dto) {
        return ResponseEntity.ok(creditCardService.updateCreditCard(id, dto));
    }

    /**
     * Deletes a credit card by ID.
     *
     * @param id the identifier of the credit card to be deleted
     * @return HTTP 204 No Content if deletion is successful
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the credit card does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
        return ResponseEntity.noContent().build();
    }

}
