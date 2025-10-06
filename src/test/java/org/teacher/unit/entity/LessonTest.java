package org.teacher.unit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teacher.entity.Lesson;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Student;
import org.teacher.entity.Teacher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class LessonTest {

    private Student student;
    private Teacher teacher;
    private Lesson lesson1;
    private Lesson lesson2;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setStudentId(1L);

        teacher = new Teacher();
        teacher.setTeacherId(1L);
        teacher.setSubject("Math");

        lesson1 = Lesson.builder()
                .lessonId(1L)
                .student(student)
                .teacher(teacher)
                .scheduledAt(LocalDateTime.of(2025, 9, 29, 10, 0))
                .durationMinutes(60)
                .price(new BigDecimal("30"))
                .status(LessonStatus.SCHEDULED)
                .homework("Do exercises 1-10")
                .feedback("Good start")
                .build();

        lesson2 = Lesson.builder()
                .lessonId(lesson1.getLessonId())
                .student(student)
                .teacher(teacher)
                .scheduledAt(LocalDateTime.of(2025, 9, 29, 10, 0))
                .durationMinutes(60)
                .price(new BigDecimal("30"))
                .status(LessonStatus.SCHEDULED)
                .homework("Do exercises 1-10")
                .feedback("Good start")
                .build();
    }

    @Test
    void testEqualsAndHashCode() {
        assertThat(lesson1).isEqualTo(lesson2);
        assertThat(lesson1.hashCode()).isEqualTo(lesson2.hashCode());

        // Изменяем lessonId → объекты не равны
        lesson2.setLessonId(2L);
        assertThat(lesson1).isNotEqualTo(lesson2);
    }

    @Test
    void testBuilderAndFields() {
        assertThat(lesson1.getStudent()).isEqualTo(student);
        assertThat(lesson1.getTeacher()).isEqualTo(teacher);
        assertThat(lesson1.getScheduledAt()).isEqualTo(LocalDateTime.of(2025, 9, 29, 10, 0));
        assertThat(lesson1.getDurationMinutes()).isEqualTo(60);
        assertThat(lesson1.getPrice()).isEqualByComparingTo(new BigDecimal("30"));
        assertThat(lesson1.getStatus()).isEqualTo(LessonStatus.SCHEDULED);
        assertThat(lesson1.getHomework()).isEqualTo("Do exercises 1-10");
        assertThat(lesson1.getFeedback()).isEqualTo("Good start");
    }

    @Test
    void testUpdateFields() {
        // Изменяем статус
        lesson1.setStatus(LessonStatus.COMPLETED);
        assertThat(lesson1.getStatus()).isEqualTo(LessonStatus.COMPLETED);

        // Изменяем длительность
        lesson1.setDurationMinutes(90);
        assertThat(lesson1.getDurationMinutes()).isEqualTo(90);

        // Изменяем цену
        lesson1.setPrice(new BigDecimal("50"));
        assertThat(lesson1.getPrice()).isEqualByComparingTo(new BigDecimal("50"));

        // Изменяем домашнее задание и отзыв
        lesson1.setHomework("Do exercises 11-20");
        lesson1.setFeedback("Excellent progress");
        assertThat(lesson1.getHomework()).isEqualTo("Do exercises 11-20");
        assertThat(lesson1.getFeedback()).isEqualTo("Excellent progress");
    }


}