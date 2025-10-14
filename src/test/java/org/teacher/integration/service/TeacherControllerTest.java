package org.teacher.integration.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.dto.StudentDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.*;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.StudentRepository;
import org.teacher.repository.StudentTeacherRepository;
import org.teacher.repository.TeacherRepository;
import org.teacher.repository.UserRepository;
import org.teacher.service.TeacherService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.teacher.integration.util.TestAuthUtils.authHeader;
import static org.teacher.integration.util.TestAuthUtils.obtainAccessToken;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentTeacherRepository studentTeacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void shouldReturnStudentsViaRest() throws Exception {
        // 1️⃣ Создаем учителя с пользователем
        var teacherUser = new UserRequestDto(
                "Petr", "Petrov", "teacher1@test.com", "secret", Set.of(Role.TEACHER)
        );
        String teacherUserJson1 = objectMapper.writeValueAsString(teacherUser);
        String savedTeacherUserJson1 = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherUserJson1))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        UserResponseDto savedTeacherUser = objectMapper.readValue(savedTeacherUserJson1, UserResponseDto.class);

        Teacher teacher = Teacher.builder()
                .user(userMapper.toEntity(savedTeacherUser))
                .subject("Math")
                .timeZone("UTC")
                .build();
        teacherRepository.save(teacher);

        // 2️⃣ Создаем учеников с пользователями
        User student1User = new User();
        student1User.setRoles(Set.of(Role.STUDENT));
        userRepository.save(student1User);

        User student2User = new User();
        student2User.setRoles(Set.of(Role.STUDENT));
        userRepository.save(student2User);

        Student student1 = Student.builder()
                .user(student1User)
                .grade(5)
                .timeZone("UTC")
                .build();

        Student student2 = Student.builder()
                .user(student2User)
                .grade(6)
                .timeZone("UTC")
                .build();

        studentRepository.saveAll(List.of(student1, student2));

        // 3️⃣ Привязываем учеников к учителю
        StudentTeacher st1 = StudentTeacher.builder()
                .teacher(teacher)
                .student(student1)
                .agreedRate(BigDecimal.valueOf(20))
                .status(StudentTeacherStatus.ACTIVE)
                .build();

        StudentTeacher st2 = StudentTeacher.builder()
                .teacher(teacher)
                .student(student2)
                .agreedRate(BigDecimal.valueOf(25))
                .status(StudentTeacherStatus.ACTIVE)
                .build();

        studentTeacherRepository.saveAll(List.of(st1, st2));


        // 4️⃣ Вызываем REST-метод через MockMvc
        MvcResult result = mockMvc.perform(get("/teachers/" + teacher.getTeacherId() + "/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,
                                authHeader(obtainAccessToken(mockMvc, objectMapper, teacherUser.email(), teacherUser.password()))))
                .andExpect(status().isOk())
                .andReturn();

        // 5️⃣ Проверяем результат
        String jsonResponse = result.getResponse().getContentAsString();
        List<StudentDto> savedStudentUser = objectMapper.readValue(
                jsonResponse,
                new TypeReference<List<StudentDto>>() {}
        );

        assertNotNull(savedStudentUser);
        assertEquals(2, savedStudentUser.size());
    }
}
