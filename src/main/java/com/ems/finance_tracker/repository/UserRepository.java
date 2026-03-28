package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository interface for the {@link User} entity.
 *
 * <p>Handles database persistence operations using Spring Data JPA for optimized
 * and secure queries.</p>
 *
 * @author Evandro Machado
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email, ensuring immediate loading of their roles.
     *
     * <p>Uses {@code LEFT JOIN FETCH} to avoid {@link org.springframework.dao.DataAccessException}
     * in security contexts where the JPA session may no longer be active.</p>
     *
     * @param email The user's email address.
     * @return an {@link Optional} containing the user if found.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmail(String email);

}
