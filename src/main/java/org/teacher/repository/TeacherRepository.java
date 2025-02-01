package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.Teacher;

@Repository
public interface  TeacherRepository extends JpaRepository<Teacher, Long> {
}
