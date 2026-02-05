package com.ems.finance_tracker.controller;

import com.ems.finance_tracker.dto.CreditCardStatementDTO;
import com.ems.finance_tracker.service.CreditCardStatementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-card-statements")
@RequiredArgsConstructor
public class CreditCardStatementController {

    private final CreditCardStatementService creditCardStatementService;

    @GetMapping
    public ResponseEntity<List<CreditCardStatementDTO.Response>> findAll() {
        return ResponseEntity.ok(creditCardStatementService.findAllCreditCardStatements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardStatementDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(creditCardStatementService.findCreditCardStatementById(id));
    }

    @PostMapping
    public ResponseEntity<CreditCardStatementDTO.Response> create(@Valid @RequestBody CreditCardStatementDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creditCardStatementService.saveCreditCardStatement(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCardStatementDTO.Response> update(@PathVariable Long id, @Valid @RequestBody CreditCardStatementDTO.Update dto) {
        return ResponseEntity.ok(creditCardStatementService.updateCreditCardStatement(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        creditCardStatementService.deleteCreditCardStatement(id);
        return ResponseEntity.noContent().build();
    }

}
