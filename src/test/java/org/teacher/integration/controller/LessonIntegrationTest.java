package org.teacher.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.teacher.dto.*;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.*;
import org.teacher.entity.LessonStatus;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.teacher.integration.util.TestAuthUtils.authHeader;
import static org.teacher.integration.util.TestAuthUtils.obtainAccessToken;

/**
 * Integration tests for Lesson entity focusing on dynamic pricing logic.

 * Scenarios tested:
 * 1. Creating a lesson without specifying a price → price should be automatically
 *    assigned from the associated StudentTeacher.agreedRate.
 * 2. Creating a lesson with a custom price → price should remain as provided.

 * These tests use MockMvc to simulate real HTTP requests to the LessonController,
 * and @Transactional ensures database state is rolled back after each test,
 * keeping tests isolated and safe for repeated execution.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class LessonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    private UserMapper userMapper;

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
    void shouldAssignPriceFromStudentTeacherIfNotProvided() throws Exception {
        // --- 4. Создаём Lesson без price ---
        LessonDto lessonDto = new LessonDto(
                                    null,
                                    savedStudent.id(),
                                    savedTeacher.id(),
                                    LocalDateTime.now().plusDays(1),
                                    60,
                                    null,
                                    LessonStatus.SCHEDULED,
                                    "Homework 1",
                                    null
                                    );

        String lessonJson = objectMapper.writeValueAsString(lessonDto);

        String savedLessonJson = mockMvc.perform(post("/lessons")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser.email(), teacherUser.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        LessonDto savedLesson = objectMapper.readValue(savedLessonJson, LessonDto.class);

        // --- Проверка: цена должна быть взята из StudentTeacher ---
        assertThat(savedLesson.price()).isEqualByComparingTo("500");
    }

    @Test
    void shouldKeepCustomPriceIfProvided() throws Exception {
        // --- Создаём Lesson с индивидуальной ценой ---
        LessonDto lessonDto = new LessonDto(
                                    null,
                                    savedStudent.id(),
                                    savedTeacher.id(),
                                    LocalDateTime.now().plusDays(1),
                                    60,
                                    new BigDecimal("750"),
                                    LessonStatus.SCHEDULED,
                                    "Homework 2",
                                    null
                            );

        String lessonJson = objectMapper.writeValueAsString(lessonDto);

        String savedLessonJson = mockMvc.perform(post("/lessons")
                        .header(HttpHeaders.AUTHORIZATION, authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser.email(), teacherUser.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        LessonDto savedLesson = objectMapper.readValue(savedLessonJson, LessonDto.class);

        // --- Проверка: цена должна остаться индивидуальной ---
        assertThat(savedLesson.price()).isEqualByComparingTo("750");
    }
}
