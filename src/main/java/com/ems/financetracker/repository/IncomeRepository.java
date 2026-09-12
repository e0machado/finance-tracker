package com.ems.financetracker.repository;

import com.ems.financetracker.model.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the {@link Income} entity.
 *
 * <p>Handles database persistence operations using Spring Data JPA.</p>
 */
public interface IncomeRepository extends JpaRepository<Income, Long> {}
