package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}

