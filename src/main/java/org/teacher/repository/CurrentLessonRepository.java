package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teacher.model.CurrentLesson;

@Repository
public interface CurrentLessonRepository extends JpaRepository<CurrentLesson, Long> {
}

