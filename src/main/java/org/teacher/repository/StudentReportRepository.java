package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.entity.StudentReport;

import java.util.Optional;

@Repository
public interface StudentReportRepository extends JpaRepository<StudentReport, Long> {

    Optional<StudentReport> findByStudent_StudentId(Long studentId);
}
