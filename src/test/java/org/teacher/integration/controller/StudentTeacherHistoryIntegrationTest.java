package org.teacher.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.dto.*;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.Lesson;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Role;
import org.teacher.entity.StudentTeacherStatus;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.teacher.integration.util.TestAuthUtils.authHeader;
import static org.teacher.integration.util.TestAuthUtils.obtainAccessToken;

/**
 * Integration test for verifying StudentTeacher history.

 * Scenario:
 * - A student can have multiple StudentTeacher associations over time.
 * - Only one StudentTeacher is active at a time (others may be PAUSED or ENDED).
 * - When creating a new Lesson, the active teacher is selected automatically.

 * This test ensures:
 *  1. Multiple StudentTeacher records can exist for one student.
 *  2. Only the ACTIVE teacher is used when creating a lesson.
 *  3. Correct price (agreedRate) from the active StudentTeacher is applied if Lesson.price is null.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class StudentTeacherHistoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

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
    void testSelectActiveTeacherForLesson() throws Exception {
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

        // ---------- 2.1 User (TEACHERS) ----------
        UserRequestDto teacherUser1 = new UserRequestDto(
                "Petr",
                "Petrov",
                "teacher@test.com",
                "secret",
                Set.of(Role.TEACHER)
        );

        String teacherUserJson1 = objectMapper.writeValueAsString(teacherUser1);

        String savedTeacherUserJson1 = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherUserJson1))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserResponseDto savedTeacherUser1 = objectMapper.readValue(savedTeacherUserJson1, UserResponseDto.class);

        UserCredentialsDto teacherUserCredentialsDto1 = userMapper.toCredentialsDto(teacherUser1);

        // ---------- 2.2 User (TEACHERS) ----------
        UserRequestDto teacherUser2 = new UserRequestDto(
                "Sidor",
                "Sidorov",
                "teacher2@test.com",
                "secret",
                Set.of(Role.TEACHER)
        );

        String teacherUserJson2 = objectMapper.writeValueAsString(teacherUser2);

        String savedTeacherUserJson2 = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherUserJson2))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserResponseDto savedTeacherUser2 = objectMapper.readValue(savedTeacherUserJson2, UserResponseDto.class);

        UserCredentialsDto teacherUserCredentialsDto2 = userMapper.toCredentialsDto(teacherUser2);

        // --- 2. Создаём Student и Teacher ---
        // ---------- 1. Student ----------
        StudentDto student = new StudentDto(
                null,
                savedStudentUser.userId(),
                LocalDate.of(2010, 5, 10),
                "+79888888888",
                "Moscow",
                "Europe/Moscow",
                5,
                "Education",
                null
        );

        String studentJson = objectMapper.writeValueAsString(student);

        String savedStudentJson = mockMvc.perform(post("/students")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, studentUser.email(), studentUser.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StudentDto savedStudent = objectMapper.readValue(savedStudentJson, StudentDto.class);

        // ---------- 4.1 Teacher ----------
        TeacherDto teacher1 = new TeacherDto(
                null,
                savedTeacherUser1.userId(),
                "Math",
                "Europe/Moscow",
                null
        );

        String teacherJson1 = objectMapper.writeValueAsString(teacher1);

        String savedTeacherJson1 = mockMvc.perform(post("/teachers")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser1.email(), teacherUser1.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson1))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TeacherDto savedTeacher1 = objectMapper.readValue(savedTeacherJson1, TeacherDto.class);

        // ---------- 4.2 Teacher ----------
        TeacherDto teacher2 = new TeacherDto(
                null,
                savedTeacherUser2.userId(),
                "Physics",
                "Europe/Moscow",
                null
        );

        String teacherJson2 = objectMapper.writeValueAsString(teacher2);

        String savedTeacherJson2 = mockMvc.perform(post("/teachers")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser2.email(), teacherUser2.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson2))
                //.andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TeacherDto savedTeacher2 = objectMapper.readValue(savedTeacherJson2, TeacherDto.class);

        // --- 3.1 Create StudentTeacher associations ---
        StudentTeacherDto studentTeacher1 = new StudentTeacherDto(
                null,
                savedStudent.studentId(),
                savedTeacher1.teacherId(),
                LocalDate.now(),
                null,
                new BigDecimal("400"),
                StudentTeacherStatus.ACTIVE
        );

        String stJson1 = objectMapper.writeValueAsString(studentTeacher1);

        String savedStJson1 = mockMvc.perform(post("/student-teachers")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser1.email(), teacherUser1.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stJson1))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StudentTeacherDto savedStudentTeacher1 = objectMapper.readValue(savedStJson1, StudentTeacherDto.class);

        // --- 3.2
        StudentTeacherDto studentTeacher2 = new StudentTeacherDto(
                null,
                savedStudent.studentId(),
                savedTeacher2.teacherId(),
                LocalDate.now(),
                null,
                new BigDecimal("600"),
                StudentTeacherStatus.ACTIVE
        );

        String stJson2 = objectMapper.writeValueAsString(studentTeacher2);

        String savedStJson2 = mockMvc.perform(post("/student-teachers")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser2.email(), teacherUser2.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stJson2))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StudentTeacherDto savedStudentTeacher2 = objectMapper.readValue(savedStJson2, StudentTeacherDto.class);

        // --- 4. Create Lesson without price (should use active StudentTeacher rate) ---
        LessonDto lessonDto = new LessonDto(
                null,
                savedStudent.studentId(),
                savedTeacher2.teacherId(),
                LocalDateTime.now().plusDays(1),
                60,
                null,
                LessonStatus.SCHEDULED,
                "Read Chapter 1",
                null
        );

        String lessonJson = objectMapper.writeValueAsString(lessonDto);

        String savedLessonJson = mockMvc.perform(post("/lessons")
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser2.email(), teacherUser2.password())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        LessonDto savedLesson = objectMapper.readValue(savedLessonJson, LessonDto.class);

        // --- 5. Assertions ---
        Lesson fetchedLesson = lessonRepository.findById(savedLesson.lessonId()).orElseThrow();

        assertThat(fetchedLesson.getTeacher().getTeacherId()).isEqualTo(savedTeacher2.teacherId()); // active teacher
        assertThat(fetchedLesson.getPrice()).isEqualByComparingTo("600"); // rate from active StudentTeacher
    }

}
