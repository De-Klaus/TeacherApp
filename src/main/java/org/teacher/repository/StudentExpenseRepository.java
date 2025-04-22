package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.StudentExpense;

@Repository
public interface StudentExpenseRepository extends JpaRepository<StudentExpense, Long> {
}

