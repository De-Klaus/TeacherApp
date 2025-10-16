package org.teacher.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.teacher.dto.*;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Role;
import org.teacher.entity.StudentTeacherStatus;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.LessonRepository;
import org.teacher.repository.StudentRepository;
import org.teacher.repository.StudentTeacherRepository;
import org.teacher.repository.TeacherRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for persistence and relationships between core entities.

 * Goal:
 *  1. Verify saving and retrieving of entities in the correct order:
 *     User → Student → Teacher → StudentTeacher → Lesson.
 *  2. Ensure that all entity relationships are persisted and can be loaded correctly
 *     (foreign keys, associations, and mappings).
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class FullFlowIntegrationTest {

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
    private UserMapper userMapper;

    private String getAccessTokenByUser(UserCredentialsDto userCredentialsDto) throws Exception {
        String loginJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokens = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokens, JwtAuthenticationDto.class);

        return jwtAuthenticationDto.token();
    }

    @Test
    void shouldCreateFullChain_UserStudentTeacherLesson() throws Exception {
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
        String studentToken = getAccessTokenByUser(studentUserCredentialsDto);

        // ---------- 2. User (TEACHER) ----------
        var teacherUser = new UserRequestDto(
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
        String teacherToken = getAccessTokenByUser(teacherUserCredentialsDto);

        // ---------- 3. Student ----------
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StudentDto savedStudent = objectMapper.readValue(savedStudentJson, StudentDto.class);

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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TeacherDto savedTeacher = objectMapper.readValue(savedTeacherJson, TeacherDto.class);

        // ---------- 5. StudentTeacher ----------
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StudentTeacherDto savedSt = objectMapper.readValue(savedStJson, StudentTeacherDto.class);

        // ---------- 6. Lesson ----------
        LessonDto lesson = new LessonDto(
                null,
                savedStudent.id(),
                savedTeacher.id(),
                LocalDateTime.now().plusDays(1),
                60,
                new BigDecimal("500"),
                new LessonStatusDto(
                        LessonStatus.SCHEDULED.name(),
                        LessonStatus.SCHEDULED.getText(),
                        LessonStatus.SCHEDULED.getColor()
                ),
                "Solve exercises 1-10",
                null
        );

        String lessonJson = objectMapper.writeValueAsString(lesson);

        String savedLessonJson = mockMvc.perform(post("/lessons")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        LessonDto savedLesson = objectMapper.readValue(savedLessonJson, LessonDto.class);

        // ---------- 7. Проверки ----------
        assertThat(studentRepository.findById(savedStudent.id())).isPresent();
        assertThat(teacherRepository.findById(savedTeacher.id())).isPresent();
        assertThat(studentTeacherRepository.findById(savedSt.id())).isPresent();
        assertThat(lessonRepository.findById(savedLesson.id())).isPresent();
    }
}
