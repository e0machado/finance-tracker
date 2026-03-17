package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for the {@link Category} entity.
 *
 * <p>Handles database persistence operations using Spring Data JPA.</p>
 *
 * @author Evandro Machado
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
