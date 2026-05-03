package com.ems.financetracker.model.mapper;

import com.ems.financetracker.dto.CostCenterDTO;
import com.ems.financetracker.model.entity.CostCenter;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting {@link CostCenterDTO} to {@link CostCenter}
 * and vice versa.
 *
 * @author Evandro Machado
 */
@Component
public class CostCenterMapper {

    /**
     * Converts a cost center creation request DTO into a Cost Center entity.
     *
     * @param dto the cost center creation request data
     * @return a new cost center entity ready for persistence
     */
    public CostCenter toEntity(CostCenterDTO.Request dto) {
        return CostCenter.builder()
                .name(dto.name())
                .build();
    }

    /**
     * Converts a Cost Center entity into a response DTO.
     *
     * @param costCenter the persisted cost center entity
     * @return a response DTO exposing cost center data
     */
    public CostCenterDTO.Response toResponse(CostCenter costCenter) {
        return new CostCenterDTO.Response(
                costCenter.getId(),
                costCenter.getName()
        );
    }

    /**
     * Updates mutable fields of an existing Cost Center entity
     * using data from an update DTO.
     *
     * <p>This method mutates the provided entity directly.</p>
     *
     * @param costCenter the existing cost center entity to be updated
     * @param dto the DTO containing updated cost center information
     */
    public void updateEntity(CostCenter costCenter, CostCenterDTO.Update dto) {
        dto.name().ifPresent(costCenter::setName);
    }

}
