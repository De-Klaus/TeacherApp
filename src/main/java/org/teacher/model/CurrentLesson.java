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
    @JoinColumn(name = "lesson_id")
    private Lesson lastLesson;
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson futureLesson;
    private String previousLessonTopic;
    private String currentLessonTopic;
    private LocalDate createdAt;
}
