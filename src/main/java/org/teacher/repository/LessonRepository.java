package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}

