package com.ems.financetracker.repository;

import com.ems.financetracker.model.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the {@link Expense} entity.
 *
 * <p>Handles database persistence operations using Spring Data JPA.</p>
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {}
