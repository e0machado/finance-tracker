package com.ems.financetracker.controller;

import com.ems.financetracker.dto.IncomeDTO;
import com.ems.financetracker.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling {@link com.ems.financetracker.model.entity.Income}
 * related HTTP requests.
 * Provides CRUD operations for income entries.
 */
@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class IncomeController {

    private final IncomeService incomeService;

    /**
     * Retrieves all income entries.
     *
     * @return HTTP 200 OK with a list of {@link IncomeDTO.Response}
     */
    @GetMapping
    public ResponseEntity<List<IncomeDTO.Response>> findAll() {
        return ResponseEntity.ok(incomeService.findAllIncomes());
    }

    /**
     * Retrieves a single income entry by ID.
     *
     * @param id the income entry identifier
     * @return HTTP 200 OK with a {@link IncomeDTO.Response}
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the income entry does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(incomeService.findIncomeById(id));
    }

    /**
     * Creates a new income entry.
     *
     * @param dto the {@link IncomeDTO.Request} containing creation data
     * @return HTTP 201 Created with a {@link IncomeDTO.Response}
     */
    @PostMapping
    public ResponseEntity<IncomeDTO.Response> create(@Valid @RequestBody IncomeDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.saveIncome(dto));
    }

    /**
     * Partially updates an existing income entry.
     *
     * @param id the identifier of the income entry to update
     * @param dto the {@link IncomeDTO.Update} containing updated data
     * @return HTTP 200 OK with a {@link IncomeDTO.Response}
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the income entry does not exist
     */
    @PatchMapping("/{id}")
    public ResponseEntity<IncomeDTO.Response> update(
            @PathVariable Long id, @Valid @RequestBody IncomeDTO.Update dto) {
        return ResponseEntity.ok(incomeService.updateIncome(id, dto));
    }

    /**
     * Deletes an income entry by ID.
     *
     * @param id the identifier of the income entry to delete
     * @return HTTP 204 No Content if deletion is successful
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the income entry does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
