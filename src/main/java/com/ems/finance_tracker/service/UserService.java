package com.ems.finance_tracker.service;

import com.ems.finance_tracker.dto.UserDTO;
import com.ems.finance_tracker.exception.BusinessException;
import com.ems.finance_tracker.exception.ResourceNotFoundException;
import com.ems.finance_tracker.model.entity.User;
import com.ems.finance_tracker.model.enums.Role;
import com.ems.finance_tracker.model.mapper.UserMapper;
import com.ems.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for managing {@link User}- related business operations.
 * Handles validation, persistence coordination and DTO/entity transformations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Retrieves all users from the system.
     *
     * @return a list of {@link UserDTO.Response} representing all registered users
     */
    public List<UserDTO.Response> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a single user by its identifier.
     *
     * @param id the user identifier
     * @return a {@link UserDTO.Response} with the user data
     * @throws ResourceNotFoundException if the user does not exist
     */
    public UserDTO.Response findUserById(Long id) {
        User user = findEntityById(id);

        return userMapper.toResponse(user);
    }

    /**
     * Creates and persists a new user.
     * Applies business rules such as email uniqueness validation,
     * password encryption and default role assignment when none is provided.
     *
     * @param dto the user creation request data
     * @return a {@link UserDTO.Response} representing the persisted user
     * @throws BusinessException if the email is already in use
     */
    @Transactional
    public UserDTO.Response saveUser(UserDTO.Request dto) {
        validateEmailUniqueness(dto.email(), null);

        String hash = passwordEncoder.encode(dto.password());
        User user = userMapper.toEntity(dto, hash);
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Collections.singleton(Role.ROLE_USER));
        }

        return userMapper.toResponse(userRepository.save(user));
    }

    /**
     * Updates an existing user's basic profile information.
     *
     * @param id the identifier of the user to be updated
     * @param dto the DTO containing updated profile data
     * @return a {@link UserDTO.Response} representing the updated user
     * @throws ResourceNotFoundException if the user does not exist
     * @throws BusinessException if the email is already in use by another user
     */
    @Transactional
    public UserDTO.Response updateUser(Long id, UserDTO.Update dto) {
        validateEmailUniqueness(dto.email(), id);

        User existingUser = findEntityById(id);

        userMapper.updateEntity(existingUser, dto);

        return userMapper.toResponse(userRepository.save(existingUser));
    }

    /**
     * Deletes a user from the system.
     *
     * @param id the identifier of the user to be deleted
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    /**
     * Retrieves a User entity by its identifier.
     *
     * @param id the user identifier
     * @return the User entity
     * @throws ResourceNotFoundException if the user does not exist
     */
    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found. ID = " + id));
    }

    /**
     * Validates whether an email address is already associated with another user.
     *
     * @param email the email address to be validated
     * @param userId the current user identifier, or null for creation
     * @throws BusinessException if the email is already in use
     */
    private void validateEmailUniqueness(String email, Long userId) {
        userRepository.findByEmail(email)
                .filter(u -> userId == null || !u.getId().equals(userId))
                .ifPresent(u -> {
                    throw new BusinessException("Email already in use");
                });

        /*
        If-based alternative:

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            if (userId == null || !u.getId().equals(userId)) {
                throw new BusinessException("Email already in use");
            }
        }

        */
    }

}
