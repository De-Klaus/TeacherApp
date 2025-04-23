package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.StudentEnvironment;

@Repository
public interface StudentEnvironmentRepository extends JpaRepository<StudentEnvironment, Long> {
}

