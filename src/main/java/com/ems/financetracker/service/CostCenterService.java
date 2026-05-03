package com.ems.financetracker.service;

import com.ems.financetracker.dto.CostCenterDTO;
import com.ems.financetracker.exception.BusinessException;
import com.ems.financetracker.exception.ResourceNotFoundException;
import com.ems.financetracker.model.entity.CostCenter;
import com.ems.financetracker.model.mapper.CostCenterMapper;
import com.ems.financetracker.repository.CostCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for managing {@link CostCenter} business operations.
 * Handles validation, persistence coordination and DTO/entity transformations.
 *
 * @author Evandro Machado
 */
@Service
@RequiredArgsConstructor
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;
    private final CostCenterMapper costCenterMapper;

    /**
     * Retrieves all cost centers from the system.
     *
     * @return a list of {@link CostCenterDTO.Response} representing all registered cost centers
     */
    public List<CostCenterDTO.Response> findAllCostCenters() {
        return costCenterRepository.findAll()
                .stream()
                .map(costCenterMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a single cost center by its identifier.
     *
     * @param id the cost center identifier
     * @return a {@link CostCenterDTO.Response} with the cost center data
     * @throws ResourceNotFoundException if the cost center does not exist
     */
    public CostCenterDTO.Response findCostCenterById(Long id) {
        CostCenter costCenter = findEntityById(id);

        return costCenterMapper.toResponse(costCenter);
    }

    /**
     * Creates e persists a new cost center.
     * Applies business rules such as name uniqueness validation.
     *
     * @param dto the cost center creation request data
     * @return a {@link CostCenterDTO.Response} representing the persisted cost center
     * @throws BusinessException if the cost center name is already in use
     */
    @Transactional
    public CostCenterDTO.Response saveCostCenter(CostCenterDTO.Request dto) {
        validateNameUniqueness(dto.name(), null);

        CostCenter costCenter = costCenterMapper.toEntity(dto);

        return costCenterMapper.toResponse(costCenterRepository.save(costCenter));
    }

    /**
     * Updates an existing cost center's information.
     *
     * @param id the identifier of the cost center to be updated
     * @param dto the DTO containing updated cost center data
     * @return a {@link CostCenterDTO.Response} representing the updated cost center
     * @throws ResourceNotFoundException if the cost center does not exist
     * @throws BusinessException if the cost center name is already in use by another cost center
     */
    @Transactional
    public CostCenterDTO.Response updateCostCenter(Long id, CostCenterDTO.Update dto) {
        if (dto.name().isPresent()) {
            validateNameUniqueness(dto.name().get(), id);
        }

        CostCenter existingCostCenter = findEntityById(id);

        costCenterMapper.updateEntity(existingCostCenter, dto);

        return costCenterMapper.toResponse(costCenterRepository.save(existingCostCenter));
    }

    /**
     * Deletes a cost center from the system.
     *
     * @param id the identifier of the cost center to be deleted
     * @throws ResourceNotFoundException if the cost center does not exist
     */
    @Transactional
    public void deleteCostCenter(Long id) {
        CostCenter costCenter = findEntityById(id);
        costCenterRepository.delete(costCenter);
    }


    /**
     * Retrieves a Cost Center entity by its identifier.
     *
     * @param id the cost center identifier
     * @return the Cost Center entity
     * @throws ResourceNotFoundException if the cost center does not exist
     */
    private CostCenter findEntityById(Long id) {
        return costCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cost center not found. ID = " + id));
    }

    /**
     * Validates whether a cost center name is already associated with another cost center.
     *
     * @param name the cost center name to be validated
     * @param costCenterId the current cost center identifier, or null for creation
     * @throws BusinessException if the cost center name is already in use
     */
    private void validateNameUniqueness(String name, Long costCenterId) {
        costCenterRepository.findByName(name)
                .filter(existing -> costCenterId == null || !existing.getId().equals(costCenterId))
                .ifPresent(existing -> {
                    throw new BusinessException("Cost center name already in use.");
                });
    }
}
