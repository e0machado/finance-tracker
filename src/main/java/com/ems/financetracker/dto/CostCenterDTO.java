package com.ems.financetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

/**
 * Interface encapsulating {@link com.ems.financetracker.model.entity.CostCenter}-related
 * Data Transfer Objects (DTOs).
 *
 * @author Evandro Machado
 */
public interface CostCenterDTO {

    /**
     * DTO used for cost center creation requests.
     */
    @Schema(name = "CostCenterRequest")
    record Request(
            @NotBlank
            @Size(max = 50)
            String name
    ) {}

    /**
     * DTO used in API responses, exposing cost center data.
     */
    @Schema(name = "CostCenterResponse")
    record Response(
            Long id,
            String name
    ) {}

    /**
     * DTO used for updating cost center information.
     */
    @Schema(name = "CostCenterUpdate")
    record Update(
            @Size(max = 50)
            Optional<@NotBlank String> name
    ) {}

}
