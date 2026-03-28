package com.ems.finance_tracker.dto;

import com.ems.finance_tracker.model.entity.User;
import com.ems.finance_tracker.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;
import java.util.Set;

/**
 * Interface encapsulating {@link User}-related Data Transfer Objects (DTOs).
 *
 * @author Evandro Machado
 */
public interface UserDTO {

    /**
     * DTO used for user registration and creation requests.
     */
    @Schema(name = "UserRequest")
    record Request(
            @NotBlank
            @Size(min = 3, max = 50)
            String name,

            @Email
            @Size(min = 6, max = 60)
            @NotBlank
            String email,

            @NotBlank
            @Size(min = 8, max = 100)
            String password
    ) {}

    /**
     * DTO used in API responses, exposing only non-sensitive user data.
     */
    @Schema(name = "UserResponse")
    record Response(
            Long id,
            String name,
            String email,
            Set<Role> roles
    ) {}

    /**
     * DTO used for updating basic user profile information,
     * excluding credentials and authorization data.
     */
    @Schema(name = "UserUpdate")
    record Update(
            @Size(min = 3, max = 50)
            Optional<@NotBlank String> name,

            @Size(min = 6, max = 60)
            Optional<@Email @NotBlank String> email
    ) {}

}
