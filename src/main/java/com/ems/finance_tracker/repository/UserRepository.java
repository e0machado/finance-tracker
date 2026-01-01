package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for the {@link User} entity.
 *
 * Handles database persistence operations using Spring Data JPA for optimized
 * and secure queries.
 *
 * @author Evandro Machado <a href="https://github.com/e0machado">Github</a>
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email, ensuring immediate loading of their roles.
     * Use of "LEFT JOIN FETCH" prevents LazyInitializationException in security contexts.
     *
     * @param email The user's email address.
     * @return An {@link Optional} containing the user if found.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

}
