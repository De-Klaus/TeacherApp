package org.teacher.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "current_lesson")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "last_lesson_id")  // Указываем уникальное имя колонки
    private Lesson lastLesson;

    @ManyToOne
    @JoinColumn(name = "future_lesson_id")  // Указываем уникальное имя колонки
    private Lesson futureLesson;
    private String previousLessonTopic;
    private String currentLessonTopic;
    private LocalDate createdAt;
}
