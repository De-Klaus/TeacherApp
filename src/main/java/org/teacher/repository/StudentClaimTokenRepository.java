package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.entity.Student;
import org.teacher.entity.StudentClaimToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentClaimTokenRepository extends JpaRepository<StudentClaimToken, Long> {
    Optional<StudentClaimToken> findByToken(UUID token);
    Optional<StudentClaimToken> findFirstByStudentAndUsedFalseAndExpiresAtAfter(Student student, LocalDateTime now);
    Optional<StudentClaimToken> findFirstByStudent_StudentIdOrderByCreatedAtDesc(Long studentId);
}