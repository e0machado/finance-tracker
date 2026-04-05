package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.CreditCardStatement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the {@link CreditCardStatement} entity.
 *
 * <p>Handles database persistence operations using Spring Data JPA.</p>
 *
 * @author Evandro Machado
 */
public interface CreditCardStatementRepository extends JpaRepository<CreditCardStatement, Long> {
    // TODO: Add domain-oriented queries as features are implemented,
    // such as findByCreditCardId, findByReferenceMonth, and aggregation queries.
    // Prefer domain-oriented queries to ensure efficient data access
    // and avoid unnecessary loading of records.
}
