package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teacher.entity.LessonBoard;

import java.util.Optional;

public interface LessonBoardRepository extends JpaRepository<LessonBoard, Long> {
    Optional<LessonBoard> findByLesson_LessonId(Long lessonId);
}
