package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.teacher.model.StudentExpense;

@RepositoryRestResource
public interface StudentExpenseRepository extends JpaRepository<StudentExpense, Long> {
}

