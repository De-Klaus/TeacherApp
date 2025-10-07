package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.entity.TeacherReport;

import java.util.Optional;

@Repository
public interface TeacherReportRepository extends JpaRepository<TeacherReport, Long> {

    Optional<TeacherReport> findByTeacher_TeacherId(Long teacherId);
}
