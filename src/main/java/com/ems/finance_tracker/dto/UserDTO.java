package com.ems.finance_tracker.dto;

import com.ems.finance_tracker.model.entity.User;
import com.ems.finance_tracker.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Interface for {@link User}- related Data Transfer Objects (DTOs).
 */
public interface UserDTO {

    /**
     * DTO used for user registration and creation requests.
     */
    record Request (
            @NotBlank
            String name,

            @Email
            @NotBlank
            String email,

            @NotBlank
            @Size(min = 8)
            String password
    ) {}

    /**
     * DTO used in API responses, exposing only non-sensitive user data.
     */
    record Response (
            Long id,
            String name,
            String email,
            Set<Role> roles
    ) {}

    /**
     * DTO used for updating basic user profile information,
     * excluding credentials and authorization data.
     */
    record Update(
            @NotBlank
            String name,

            @Email
            @NotBlank
            String email
    ) {}

}
