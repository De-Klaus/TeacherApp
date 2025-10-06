package org.teacher.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.teacher.entity.*;
import org.teacher.service.StudentBalanceService;
import org.teacher.service.StudentService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link StudentService}.

 * Goal:
 *  - Verify CRUD operations for Lesson.
 *  - Ensure business rules for scheduling (e.g., student cannot have overlapping lessons).
 *  - Validate mapping between Lesson and LessonDto.
 */
@SpringBootTest
class StudentServiceTest {

    @Autowired
    private StudentBalanceService balanceService;
    private Student student;
    private Teacher teacher;


    @BeforeEach
    void setUp() {
        student = Student.builder()
                .studentId(1L)
                .build();

        teacher = Teacher.builder()
                .teacherId(1L)
                .subject("Math")
                .timeZone("GMT+7")
                .build();
    }

    @Test
    void testBalanceWithSingleLessonAndSinglePayment() {
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .student(student)
                .teacher(teacher)
                .price(new BigDecimal("50"))
                .status(LessonStatus.COMPLETED)
                .build();

        student.getLessons().add(lesson);

        Payment payment = Payment.builder()
                .amount(new BigDecimal("70"))
                .build();

        BigDecimal balance = balanceService.calculateBalance(student, List.of(payment));

        // баланс = 70 − 50 = 20
        assertThat(balance).isEqualByComparingTo(new BigDecimal("20"));
    }

    @Test
    void testBalanceWithMultipleLessonsAndPayments() {
        Lesson lesson1 = Lesson.builder()
                .lessonId(1L)
                .student(student)
                .teacher(teacher)
                .price(new BigDecimal("50"))
                .status(LessonStatus.COMPLETED)
                .build();

        Lesson lesson2 = Lesson.builder()
                .lessonId(1L)
                .student(student)
                .teacher(teacher)
                .price(new BigDecimal("30"))
                .status(LessonStatus.COMPLETED)
                .build();

        Lesson lesson3 = Lesson.builder()
                .lessonId(1L)
                .student(student)
                .teacher(teacher)
                .price(new BigDecimal("40"))
                .status(LessonStatus.SCHEDULED) // не завершён, не учитываем
                .build();

        student.getLessons().addAll(Arrays.asList(lesson1, lesson2, lesson3));

        Payment payment1 = Payment.builder()
                .amount(new BigDecimal("60"))
                .build();

        Payment payment2 = Payment.builder()
                .amount(new BigDecimal("50"))
                .build();

        BigDecimal balance = balanceService.calculateBalance(student, Arrays.asList(payment1, payment2));

        // баланс = (60+50) − (50+30) = 110 − 80 = 30
        assertThat(balance).isEqualByComparingTo(new BigDecimal("30"));
    }

    @Test
    void testBalanceWithNoCompletedLessons() {
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .student(student)
                .teacher(teacher)
                .price(new BigDecimal("50"))
                .status(LessonStatus.SCHEDULED)
                .build();

        student.getLessons().add(lesson);

        Payment payment = Payment.builder()
                .amount(new BigDecimal("40"))
                .build();

        BigDecimal balance = balanceService.calculateBalance(student, List.of(payment));

        // баланс = 40 − 0 = 40
        assertThat(balance).isEqualByComparingTo(new BigDecimal("40"));
    }

    @Test
    void testBalanceWithNoPayments() {
        Lesson lesson = Lesson.builder()
                .lessonId(1L)
                .student(student)
                .teacher(teacher)
                .price(new BigDecimal("50"))
                .status(LessonStatus.COMPLETED)
                .build();

        student.getLessons().add(lesson);

        BigDecimal balance = balanceService.calculateBalance(student, List.of());

        // баланс = 0 − 50 = -50
        assertThat(balance).isEqualByComparingTo(new BigDecimal("-50"));
    }

}