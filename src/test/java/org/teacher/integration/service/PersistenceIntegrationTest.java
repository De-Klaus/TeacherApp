package org.teacher.integration.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.teacher.entity.*;
import org.teacher.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // автоматически откатывает изменения после теста
public class PersistenceIntegrationTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentTeacherRepository studentTeacherRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Test
    void testPersistUserStudentTeacherLessonChain() {
        // --- 1. Создаём User ---
        User studentUser = User.builder()
                .userId(UUID.randomUUID())
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("student@test.com")
                .password("secret")
                .roles(Set.of(Role.STUDENT))
                .build();

        User teacherUser = User.builder()
                .userId(UUID.randomUUID())
                .firstName("Petr")
                .lastName("Petrov")
                .email("teacher@test.com")
                .password("secret")
                .roles(Set.of(Role.TEACHER))
                .build();

        User su = userRepository.save(studentUser);
        User tu =userRepository.save(teacherUser);

        // --- 2. Создаём Student + Teacher ---
        Student student = Student.builder()
                .user(su)
                .birthDate(LocalDate.of(2010, 5, 10))
                .timeZone("Europe/Moscow")
                .grade(5)
                .city("Moscow")
                .build();

        Teacher teacher = Teacher.builder()
                .user(tu)
                .subject("Math")
                .timeZone("GMT+7")
                .build();

        studentRepository.save(student);
        teacherRepository.save(teacher);

        // --- 3. Создаём связь StudentTeacher ---
        StudentTeacher st = StudentTeacher.builder()
                .student(student)
                .teacher(teacher)
                .startDate(LocalDate.now())
                .agreedRate(new BigDecimal("500"))
                .status(StudentTeacherStatus.ACTIVE)
                .build();

        studentTeacherRepository.save(st);

        // --- 4. Создаём Lesson ---
        Lesson lesson = Lesson.builder()
                .student(student)
                .teacher(teacher)
                .scheduledAt(LocalDateTime.now().plusDays(1))
                .durationMinutes(60)
                .price(new BigDecimal("500"))
                .status(LessonStatus.SCHEDULED)
                .homework("Solve exercises 1-10")
                .feedback(null)
                .build();

        lessonRepository.save(lesson);

        // --- 5. Проверки ---
        Student fetchedStudent = studentRepository.findById(student.getStudentId()).orElseThrow();
        Teacher fetchedTeacher = teacherRepository.findById(teacher.getTeacherId()).orElseThrow();
        Lesson fetchedLesson = lessonRepository.findById(lesson.getLessonId()).orElseThrow();
        StudentTeacher fetchedST = studentTeacherRepository.findById(st.getStudentTeacherId()).orElseThrow();

        assertThat(fetchedStudent.getUser().getEmail()).isEqualTo("student@test.com");
        assertThat(fetchedTeacher.getUser().getEmail()).isEqualTo("teacher@test.com");

        assertThat(fetchedST.getStudent().getStudentId()).isEqualTo(fetchedStudent.getStudentId());
        assertThat(fetchedST.getTeacher().getTeacherId()).isEqualTo(fetchedTeacher.getTeacherId());

        assertThat(fetchedLesson.getStudent().getStudentId()).isEqualTo(fetchedStudent.getStudentId());
        assertThat(fetchedLesson.getTeacher().getTeacherId()).isEqualTo(fetchedTeacher.getTeacherId());
        assertThat(fetchedLesson.getPrice()).isEqualByComparingTo("500");
    }
}
