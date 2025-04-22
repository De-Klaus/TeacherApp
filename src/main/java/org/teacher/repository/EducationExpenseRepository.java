package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.EducationExpense;

@Repository
public interface EducationExpenseRepository extends JpaRepository<EducationExpense, Long> {
}

