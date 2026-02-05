package com.ems.finance_tracker.repository;

import com.ems.finance_tracker.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
