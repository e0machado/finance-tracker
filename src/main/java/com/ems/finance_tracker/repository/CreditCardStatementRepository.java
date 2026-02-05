package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.CreditCardStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardStatementRepository extends JpaRepository<CreditCardStatement, Long> {}
