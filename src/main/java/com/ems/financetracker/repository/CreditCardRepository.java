package com.ems.financetracker.repository;

import com.ems.financetracker.model.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the {@link CreditCard} entity.
 *
 * <p>Handles database persistence operations using Spring Data JPA for optimized
 * and secure queries.</p>
 *
 * @author Evandro Machado
 */
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {}
