package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.teacher.model.Lesson;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("SELECT l.studentName, COUNT(l), SUM(l.price) FROM Lesson l WHERE l.user.id = :userId GROUP BY l.studentName")
    List<Object[]> getLessonSummary(@Param("userId") Long userId);
}