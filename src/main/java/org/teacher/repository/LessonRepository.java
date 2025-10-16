package org.teacher.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.entity.Lesson;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Student;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByStudentAndStatus(Student student, LessonStatus status);
    List<Lesson> findByTeacher_TeacherId(Long teacherId);
    List<Lesson> findByStudent_StudentId(Long studentId);
    List<Lesson> findAllByTeacher_User_UserId(UUID userId);
    List<Lesson> findAllByStudent_User_UserId(UUID userId);
    Page<Lesson> findAllByStudent_User_UserId(UUID userId, Pageable pageable);
    Page<Lesson> findAllByTeacher_User_UserId(UUID userId, Pageable pageable);
}
