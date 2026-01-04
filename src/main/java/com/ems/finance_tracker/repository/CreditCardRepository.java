package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the {@link CreditCard} entity.
 *
 * Handles database persistence operations using Spring Data JPA for optimized
 * and secure queries.
 */
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {}
