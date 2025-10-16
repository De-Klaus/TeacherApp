package org.teacher.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.teacher.dto.*;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.*;
import org.teacher.entity.LessonStatus;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.*;
import org.teacher.service.StudentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.teacher.integration.util.TestAuthUtils.authHeader;
import static org.teacher.integration.util.TestAuthUtils.obtainAccessToken;
import static org.assertj.core.api.Assertions.assertThat;



/**
 * Integration test for Student balance calculation.

 * Scenario:
 * 1. Create a student, teacher, StudentTeacher.
 * 2. Create multiple lessons with different statuses.
 * 3. Create multiple payments for the student.
 * 4. Verify that balance is correctly calculated via StudentService.
 */

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class StudentBalanceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentTeacherRepository studentTeacherRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentService studentService;

    private StudentDto savedStudent;
    private TeacherDto savedTeacher;
    private UserRequestDto teacherUser;

    @BeforeEach
    void setUp() throws Exception {
        // --- 1. Создаём User студента и учителя ---
        // ---------- 1. User (STUDENT) ----------
        var studentUser = new UserRequestDto(
                "Ivan",
                "Ivanov",
                "student@test.com",
                "secret",
                Set.of(Role.STUDENT)
        );

        String studentUserJson = objectMapper.writeValueAsString(studentUser);

        String savedStudentUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentUserJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserResponseDto savedStudentUser = objectMapper.readValue(savedStudentUserJson, UserResponseDto.class);

        UserCredentialsDto studentUserCredentialsDto = userMapper.toCredentialsDto(studentUser);

        // ---------- 2. User (TEACHER) ----------
        teacherUser = new UserRequestDto(
                "Petr",
                "Petrov",
                "teacher@test.com",
                "secret",
                Set.of(Role.TEACHER)
        );

        String teacherUserJson = objectMapper.writeValueAsString(teacherUser);

        String savedTeacherUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherUserJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserResponseDto savedTeacherUser = objectMapper.readValue(savedTeacherUserJson, UserResponseDto.class);

        UserCredentialsDto teacherUserCredentialsDto = userMapper.toCredentialsDto(teacherUser);

        // --- 2. Создаём Student и Teacher ---
        // ---------- 1. Student ----------
        StudentDto student = new StudentDto(
                null,
                savedStudentUser.userId(),
                savedStudentUser.firstName(),
                savedStudentUser.lastName(),
                LocalDate.of(2010, 5, 10),
                "+79888888888",
                "Moscow",
                "Europe/Moscow",
                5,
                "Education"
        );

        String studentJson = objectMapper.writeValueAsString(student);

        String savedStudentJson = mockMvc.perform(post("/students")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, studentUser.email(), studentUser.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        savedStudent = objectMapper.readValue(savedStudentJson, StudentDto.class);

        // ---------- 4. Teacher ----------
        TeacherDto teacher = new TeacherDto(
                null,
                savedTeacherUser.userId(),
                savedTeacherUser.firstName(),
                savedTeacherUser.lastName(),
                "Math",
                "Europe/Moscow"
        );

        String teacherJson = objectMapper.writeValueAsString(teacher);

        String savedTeacherJson = mockMvc.perform(post("/teachers")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser.email(), teacherUser.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        savedTeacher = objectMapper.readValue(savedTeacherJson, TeacherDto.class);

        // --- 3. Создаём StudentTeacher с agreedRate ---
        StudentTeacherDto studentTeacher = new StudentTeacherDto(
                null,
                savedStudent.id(),
                savedTeacher.id(),
                LocalDate.now(),
                null,
                new BigDecimal("500"),
                StudentTeacherStatus.ACTIVE
        );

        String stJson = objectMapper.writeValueAsString(studentTeacher);

        String savedStJson = mockMvc.perform(post("/student-teachers")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser.email(), teacherUser.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StudentTeacherDto savedStudentTeacher = objectMapper.readValue(savedStJson, StudentTeacherDto.class);
    }

    @Test
    void shouldCalculateCorrectBalance() throws Exception {
        // --- 4. Создаём уроки с разными статусами ---
        LessonDto lesson1 = new LessonDto(
                null,
                savedStudent.id(),
                savedTeacher.id(),
                LocalDateTime.now().minusDays(2),
                60,
                new BigDecimal("500"),
                LessonStatus.COMPLETED,
                null,
                null
        );

        String savedLessonJson1 = saveLessonFromApi(lesson1);

        LessonDto lesson2 = new LessonDto(
                null,
                savedStudent.id(),
                savedTeacher.id(),
                LocalDateTime.now().minusDays(1),
                60,
                new BigDecimal("600"),
                LessonStatus.SCHEDULED,
                null,
                null
        );
        String savedLessonJson2 = saveLessonFromApi(lesson2);

        LessonDto lesson3 = new LessonDto(
                null,
                savedStudent.id(),
                savedTeacher.id(),
                LocalDateTime.now(),
                60,
                new BigDecimal("400"),
                LessonStatus.CANCELED,
                null,
                null
        );
        String savedLessonJson3 = saveLessonFromApi(lesson3);

        // --- 5. Создаём платежи ---
        PaymentDto paymentDto1 = new PaymentDto(
                null,
                savedStudent.id(),
                LocalDateTime.now().minusDays(3),
                new BigDecimal("700"),
                PaymentMethod.TRANSFER,
                "234523452345"
        );
        String savedPaymentJson1 = savePaymentFromApi(paymentDto1);

        PaymentDto paymentDto2 = new PaymentDto(
                null,
                savedStudent.id(),
                LocalDateTime.now().minusDays(2),
                new BigDecimal("300"),
                PaymentMethod.TRANSFER,
                "234523454545"
        );
        String savedPaymentJson2 = savePaymentFromApi(paymentDto2);

        // --- 6. Расчёт баланса через сервис ---
        BigDecimal balance = studentService.calculateBalance(savedStudent.id());

        // --- Проверка ---
        // COMPLETED уроки: lesson1 = 500
        // totalPaid = 700 + 300 = 1000
        // balance = 1000 - 500 = 500
        assertThat(balance).isEqualByComparingTo(new BigDecimal("500"));
    }

    private String saveLessonFromApi(LessonDto lesson) throws Exception {
        String lessonJson = objectMapper.writeValueAsString(lesson);

        return mockMvc.perform(post("/lessons")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser.email(), teacherUser.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    private String savePaymentFromApi(PaymentDto payment) throws Exception {
        String paymentJson = objectMapper.writeValueAsString(payment);

        return mockMvc.perform(post("/payments")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser.email(), teacherUser.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }
}
