package com.ems.financetracker.controller;

import com.ems.financetracker.dto.ExpenseDTO;
import com.ems.financetracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling {@link com.ems.financetracker.model.entity.Expense}
 * related HTTP requests.
 * Provides CRUD operations for expense entries.
 */
@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * Retrieves all expense entries.
     *
     * @return HTTP 200 OK with a list of {@link ExpenseDTO.Response}
     */
    @GetMapping
    public ResponseEntity<List<ExpenseDTO.Response>> findAll() {
        return ResponseEntity.ok(expenseService.findAllExpenses());
    }

    /**
     * Retrieves a single expense entry by ID.
     *
     * @param id the expense entry identifier
     * @return HTTP 200 OK with a {@link ExpenseDTO.Response}
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the expense entry does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.findExpenseById(id));
    }

    /**
     * Creates a new expense entry.
     *
     * @param dto the {@link ExpenseDTO.Request} containing creation data
     * @return HTTP 201 Created with a {@link ExpenseDTO.Response}
     */
    @PostMapping
    public ResponseEntity<ExpenseDTO.Response> create(@Valid @RequestBody ExpenseDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.saveExpense(dto));
    }

    /**
     * Partially updates an existing expense entry.
     *
     * @param id the identifier of the expense entry to update
     * @param dto the {@link ExpenseDTO.Update} containing updated data
     * @return HTTP 200 OK with a {@link ExpenseDTO.Response}
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the expense entry does not exist
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ExpenseDTO.Response> update(
            @PathVariable Long id, @Valid @RequestBody ExpenseDTO.Update dto) {
        return ResponseEntity.ok(expenseService.updateExpense(id, dto));
    }

    /**
     * Deletes an expense entry by ID.
     *
     * @param id the identifier of the expense entry to delete
     * @return HTTP 204 No Content if deletion is successful
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the expense entry does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
