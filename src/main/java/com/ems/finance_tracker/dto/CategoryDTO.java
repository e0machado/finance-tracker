package com.ems.finance_tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

/**
 * Interface encapsulating {@link com.ems.finance_tracker.model.entity.Category}-related
 * Data Transfer Objects (DTOs).
 *
 * @author Evandro Machado
 */
public interface CategoryDTO {

    /**
     * DTO used for category creation requests.
     */
    @Schema(name = "CategoryRequest")
    record Request(
            @NotBlank
            @Size(max = 50)
            String name
    ) {}

    /**
     * DTO used in API responses, exposing category data.
     */
    @Schema(name = "CategoryResponse")
    record Response(
            Long id,
            String name
    ) {}

    /**
     * DTO used for updating category information.
     */
    @Schema(name = "CategoryUpdate")
    record Update(
            @Size(max = 50)
            Optional<@NotBlank String> name
    ) {}

}
