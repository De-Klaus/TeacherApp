package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.entity.Lesson;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Student;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByStudentAndStatus(Student student, LessonStatus status);
    List<Lesson> findByTeacher_TeacherId(Long teacherId);
    List<Lesson> findByStudent_StudentId(Long studentId);
}
