package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.CreditCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for the {@link CreditCardTransaction} entity.
 *
 * <p>Handles database persistence operations using Spring Data JPA.</p>
 *
 * @author Evandro Machado
 */
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long> {
    // TODO: Add domain-oriented queries as features are implemented,
    // such as findByCreditCardId, findByStatementId, and aggregation queries
    // (e.g. sumExpensesByCard). Prefer domain-oriented queries to ensure efficient
    // data access and avoid unnecessary loading of records.
}
