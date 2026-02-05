package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.CreditCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long> {}
