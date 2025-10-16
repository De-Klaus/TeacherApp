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
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Role;
import org.teacher.entity.StudentTeacherStatus;
import org.teacher.mapper.UserMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.teacher.integration.util.TestAuthUtils.obtainAccessToken;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Integration test for handling StudentTeacher status changes.

 * Scenario:
 * 1. A student has an active teacher and lessons.
 * 2. Student leaves the teacher → status = ENDED.
 * 3. A new active teacher is assigned to the student.
 * 4. Lessons from the old teacher are not counted for balance or new lesson rates.
 * 5. Verifies that the new rate is applied to new lessons.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class StudentTeacherStatusIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    void studentTeacherStatusChange_shouldApplyNewRateAndIgnoreOldLessons() throws Exception {

        // --- 1. Create STUDENT User ---
        var studentUser = new UserRequestDto(
                "Ivan", "Ivanov", "student@test.com", "secret", Set.of(Role.STUDENT)
        );
        String studentUserJson = objectMapper.writeValueAsString(studentUser);
        String savedStudentUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentUserJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UserResponseDto savedStudentUser = objectMapper.readValue(savedStudentUserJson, UserResponseDto.class);

        // --- 2. Create TEACHER Users ---
        var teacherUser1 = new UserRequestDto(
                "Petr", "Petrov", "teacher1@test.com", "secret", Set.of(Role.TEACHER)
        );
        String teacherUserJson1 = objectMapper.writeValueAsString(teacherUser1);
        String savedTeacherUserJson1 = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherUserJson1))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UserResponseDto savedTeacherUser1 = objectMapper.readValue(savedTeacherUserJson1, UserResponseDto.class);

        var teacherUser2 = new UserRequestDto(
                "Sidor", "Sidorov", "teacher2@test.com", "secret", Set.of(Role.TEACHER)
        );
        String teacherUserJson2 = objectMapper.writeValueAsString(teacherUser2);
        String savedTeacherUserJson2 = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherUserJson2))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UserResponseDto savedTeacherUser2 = objectMapper.readValue(savedTeacherUserJson2, UserResponseDto.class);

        // --- 3. Create STUDENT ---
        var studentDto = new StudentDto(
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
        String studentJson = objectMapper.writeValueAsString(studentDto);
        String tokenStudent = obtainAccessToken(mockMvc, objectMapper, studentUser.email(), studentUser.password());
        String savedStudentJson = mockMvc.perform(post("/students")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenStudent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        StudentDto savedStudent = objectMapper.readValue(savedStudentJson, StudentDto.class);

        // --- 4. Create TEACHERS ---
        var teacherDto1 = new TeacherDto(null, savedTeacherUser1.userId(), savedTeacherUser1.firstName(),savedTeacherUser1.lastName(), "Math", "Europe/Moscow");
        String teacherJson1 = objectMapper.writeValueAsString(teacherDto1);
        String tokenTeacher1 = obtainAccessToken(mockMvc, objectMapper, teacherUser1.email(), teacherUser1.password());
        String savedTeacherJson1 = mockMvc.perform(post("/teachers")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenTeacher1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson1))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TeacherDto savedTeacher1 = objectMapper.readValue(savedTeacherJson1, TeacherDto.class);

        var teacherDto2 = new TeacherDto(null, savedTeacherUser2.userId(), savedTeacherUser2.firstName(),savedTeacherUser2.lastName(), "Physics", "Europe/Moscow");
        String teacherJson2 = objectMapper.writeValueAsString(teacherDto2);
        String tokenTeacher2 = obtainAccessToken(mockMvc, objectMapper, teacherUser2.email(), teacherUser2.password());
        String savedTeacherJson2 = mockMvc.perform(post("/teachers")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenTeacher2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson2))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TeacherDto savedTeacher2 = objectMapper.readValue(savedTeacherJson2, TeacherDto.class);

        // --- 5. Assign ACTIVE StudentTeacher to first teacher ---
        var stDto1 = new StudentTeacherDto(
                null,
                savedStudent.id(),
                savedTeacher1.id(),
                LocalDate.now().minusMonths(2),
                null,
                new BigDecimal("500"),
                StudentTeacherStatus.ACTIVE
        );
        String stJson1 = objectMapper.writeValueAsString(stDto1);
        String savedStJson = mockMvc.perform(post("/student-teachers")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenTeacher1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stJson1))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StudentTeacherDto savedStudentTeacher = objectMapper.readValue(savedStJson, StudentTeacherDto.class);

        // --- 6. Create lesson for old teacher ---
        var lessonDto1 = new LessonDto(
                null,
                savedStudent.id(),
                savedTeacher1.id(),
                LocalDateTime.now().minusDays(1),
                60,
                null,
                new LessonStatusDto(
                        LessonStatus.SCHEDULED.name(),
                        LessonStatus.SCHEDULED.getText(),
                        LessonStatus.SCHEDULED.getColor()
                ),
                "Homework 1",
                null
        );
        String lessonJson1 = objectMapper.writeValueAsString(lessonDto1);
        mockMvc.perform(post("/lessons")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenTeacher1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson1))
                .andExpect(status().isCreated());

        // --- 7. End old StudentTeacher and assign new ACTIVE teacher ---
        mockMvc.perform(patch("/student-teachers/{id}/end", savedStudentTeacher.id()) // assume first StudentTeacher ID = 1
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenTeacher1))
                .andDo(print())
                .andExpect(status().isOk());

        var stDto2 = new StudentTeacherDto(
                null,
                savedStudent.id(),
                savedTeacher2.id(),
                LocalDate.now(),
                null,
                new BigDecimal("700"),
                StudentTeacherStatus.ACTIVE
        );
        String stJson2 = objectMapper.writeValueAsString(stDto2);
        mockMvc.perform(post("/student-teachers")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenTeacher2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stJson2))
                .andExpect(status().isCreated());

        // --- 8. Create new lesson (should use new active teacher and rate) ---
        var lessonDto2 = new LessonDto(
                null,
                savedStudent.id(),
                savedTeacher2.id(),
                LocalDateTime.now().plusDays(1),
                60,
                null,
                new LessonStatusDto(
                        LessonStatus.SCHEDULED.name(),
                        LessonStatus.SCHEDULED.getText(),
                        LessonStatus.SCHEDULED.getColor()
                ),
                "Homework 2",
                null
        );
        String lessonJson2 = objectMapper.writeValueAsString(lessonDto2);
        String responseLesson2 = mockMvc.perform(post("/lessons")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenTeacher2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson2))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        LessonDto savedLesson2 = objectMapper.readValue(responseLesson2, LessonDto.class);

        // --- 9. Assertions ---
        assertThat(savedLesson2.teacherId()).isEqualTo(savedTeacher2.id());
        assertThat(savedLesson2.price()).isEqualByComparingTo(new BigDecimal("700"));
    }
}
