package com.ems.finance_tracker.model.mapper;

import com.ems.finance_tracker.dto.UserDTO;
import com.ems.finance_tracker.model.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting {@link UserDTO} to {@link User} entities and vice versa.
 */
@Component
public class UserMapper {

    /**
     * Converts a User creation request DTO into a User entity.
     *
     * @param dto the user creation request data
     * @param passwordHash the already encrypted user password
     * @return a new User entity ready for persistence
     */
    public User toEntity(UserDTO.Request dto, String passwordHash) {
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .passwordHash(passwordHash)
                .build();
    }

    /**
     * Converts a User entity into a safe response DTO.
     *
     * @param user the persisted user entity
     * @return a response DTO exposing only non-sensitive user data
     */
    public UserDTO.Response toResponse(User user) {
        return new UserDTO.Response(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles()
        );
    }

    /**
     * Updates mutable fields of an existing User entity
     * using data from an update DTO.
     *
     * @param user the existing user entity to be updated
     * @param dto the DTO containing updated profile information
     */
    public void updateEntity(User user, UserDTO.Update dto) {
        user.setName(dto.name());
        user.setEmail(dto.email());
    }

}
