package com.ems.financetracker.repository;

import com.ems.financetracker.model.entity.Category;
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
