package com.ems.finance_tracker.controller;

import com.ems.finance_tracker.dto.UserDTO;
import com.ems.finance_tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for handling {@link com.ems.finance_tracker.model.entity.User}- related HTTP requests.
 * Provides CRUD operations for users.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @return HTTP 200 OK with a list of {@link UserDTO.Response} representing all users
     */
    @GetMapping
    public ResponseEntity<List<UserDTO.Response>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    /**
     * Retrieves a single user by ID.
     *
     * @param id the user identifier
     * @return HTTP 200 OK with a {@link UserDTO.Response} containing user data
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the user does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.Response> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    /**
     * Creates a new user.
     *
     * @param dto the {@link UserDTO.Request} containing user creation data
     * @return HTTP 201 Created with a {@link UserDTO.Response} representing the persisted user
     * @throws com.ems.finance_tracker.exception.BusinessException if the email is already in use
     */
    @PostMapping
    public ResponseEntity<UserDTO.Response> createUser(@Valid @RequestBody UserDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(dto));
    }

    /**
     * Updates an existing user's basic profile information.
     *
     * @param id the identifier of the user to be updated
     * @param dto the {@link UserDTO.Update} containing updated user data
     * @return HTTP 200 OK with a {@link UserDTO.Response} representing the updated user
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the user does not exist
     * @throws com.ems.finance_tracker.exception.BusinessException if the email is already in use by another user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO.Response> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO.Update dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the identifier of the user to be deleted
     * @return HTTP 204 No Content if deletion is successful
     * @throws com.ems.finance_tracker.exception.ResourceNotFoundException if the user does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
