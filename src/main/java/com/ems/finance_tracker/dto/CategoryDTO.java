package com.ems.finance_tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public interface CategoryDTO {

    @Schema(name = "CategoryRequest")
    record Request(
            @NotBlank
            @Size(max = 50)
            String name
    ) {}

    @Schema(name = "CategoryResponse")
    record Response(
            Long id,
            String name
    ) {}

    @Schema(name = "CategoryUpdate")
    record Update(
            @Size(max = 50)
            Optional<@NotBlank String> name
    ) {}

}
