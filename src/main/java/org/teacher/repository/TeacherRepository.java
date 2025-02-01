package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teacher.model.Teacher;

public interface  TeacherRepository extends JpaRepository<Teacher, Long> {
}
