package org.teacher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private LocalDateTime scheduledAt;   // дата и время урока
    private Integer durationMinutes;     // длительность

    private BigDecimal price;            // стоимость за занятие (фиксируется в момент создания)

    @Enumerated(EnumType.STRING)
    private LessonStatus status;         // SCHEDULED, COMPLETED, CANCELED

    // Тема урока / заметка
    @Column(length = 2000)
    private String topic;

    // Домашнее задание
    @Column(length = 2000)
    private String homework;

    // Обратная связь
    @Column(length = 2000)
    private String feedback;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(lessonId, lesson.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId);
    }
}
