package com.ems.finance_tracker.controller;

import com.ems.finance_tracker.dto.CreditCardTransactionDTO;
import com.ems.finance_tracker.service.CreditCardTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-card-transactions")
@RequiredArgsConstructor
public class CreditCardTransactionController {

    private final CreditCardTransactionService creditCardTransactionService;

    @GetMapping
    public ResponseEntity<List<CreditCardTransactionDTO.Response>> findAll() {
        return ResponseEntity.ok(creditCardTransactionService.findAllCreditCardTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardTransactionDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(creditCardTransactionService.findCreditCardTransactionById(id));
    }

    @PostMapping
    public ResponseEntity<CreditCardTransactionDTO.Response> create(@Valid @RequestBody CreditCardTransactionDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creditCardTransactionService.saveCreditCardTransaction(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCardTransactionDTO.Response> update(@PathVariable Long id, @Valid @RequestBody CreditCardTransactionDTO.Update dto) {
        return ResponseEntity.ok(creditCardTransactionService.updateCreditCardTransaction(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        creditCardTransactionService.deleteCreditCardTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
