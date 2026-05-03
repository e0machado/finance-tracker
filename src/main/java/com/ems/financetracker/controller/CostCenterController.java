package com.ems.financetracker.controller;

import com.ems.financetracker.dto.CostCenterDTO;
import com.ems.financetracker.service.CostCenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling {@link com.ems.financetracker.model.entity.CostCenter}
 * related HTTP requests.
 * Provides CRUD operations for cost centers.
 *
 * @author Evandro Machado
 */
@RestController
@RequestMapping("/cost-centers")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class CostCenterController {

    private final CostCenterService costCenterService;

    /**
     * Retrieves all cost centers.
     *
     * @return HTTP 200 OK with a list of {@link CostCenterDTO.Response} representing all cost centers
     */
    @GetMapping
    public ResponseEntity<List<CostCenterDTO.Response>> findAllCostCenters() {
        return ResponseEntity.ok(costCenterService.findAllCostCenters());
    }

    /**
     * Retrieves a single cost center by ID.
     *
     * @param id the cost center identifier
     * @return HTTP 200 OK with a {@link CostCenterDTO.Response} containing cost center data
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the cost center does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<CostCenterDTO.Response> findCostCenterById(@PathVariable Long id) {
        return ResponseEntity.ok(costCenterService.findCostCenterById(id));
    }

    /**
     * Creates a new cost center.
     *
     * @param dto the {@link CostCenterDTO.Request} containing cost center creation data
     * @return HTTP 201 Created with a {@link CostCenterDTO.Response} representing the persisted cost center
     * @throws com.ems.financetracker.exception.BusinessException if the cost center name is already in use
     */
    @PostMapping
    public ResponseEntity<CostCenterDTO.Response> createCostCenter(@Valid @RequestBody CostCenterDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(costCenterService.saveCostCenter(dto));
    }

    /**
     * Updates an existing cost center's information.
     *
     * @param id the identifier of the cost center to be updated
     * @param dto the {@link CostCenterDTO.Update} containing updated cost center data
     * @return HTTP 200 OK with a {@link CostCenterDTO.Response} representing the updated cost center
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the cost center does not exist
     * @throws com.ems.financetracker.exception.BusinessException if the cost center name is already in use by another cost center
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CostCenterDTO.Response> updateCostCenter(@PathVariable Long id, @Valid @RequestBody CostCenterDTO.Update dto) {
        return ResponseEntity.ok(costCenterService.updateCostCenter(id, dto));
    }

    /**
     * Deletes a cost center by ID.
     *
     * @param id the identifier of the cost center to be deleted
     * @return HTTP 204 No Content if deletion is successful
     * @throws com.ems.financetracker.exception.ResourceNotFoundException if the cost center does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostCenter(@PathVariable Long id) {
        costCenterService.deleteCostCenter(id);
        return ResponseEntity.noContent().build();
    }

}
