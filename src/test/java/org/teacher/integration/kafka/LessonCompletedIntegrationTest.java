package org.teacher.integration.kafka;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.teacher.dto.*;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Role;
import org.teacher.entity.StudentTeacherStatus;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.*;
import org.teacher.service.LessonService;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.teacher.integration.util.TestAuthUtils.authHeader;
import static org.teacher.integration.util.TestAuthUtils.obtainAccessToken;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = { "lessons.completed.events" }, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext
@AutoConfigureMockMvc
//@Transactional
public class LessonCompletedIntegrationTest extends AbstractKafkaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private StudentReportRepository studentReportRepository;

    @Autowired
    private TeacherReportRepository teacherReportRepository;

    @Autowired
    private DataSource dataSource;

    private StudentDto savedStudent;
    private TeacherDto savedTeacher;


    /*@Test
    void verifyH2Used() throws Exception {
        String jdbcUrl = dataSource.getConnection().getMetaData().getURL();
        assertThat(jdbcUrl).isEqualTo("jdbc:h2:mem:testdb");
    }*/

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
    void addLesson_shouldPublishAndConsumeLessonCompletedEvent() throws InterruptedException {
        var studentId = savedStudent.id();
        var teacherId = savedTeacher.id();
        // 1. Создаем DTO урока
        LessonDto lessonDto = new LessonDto(
                null,
                studentId,
                teacherId,
                LocalDateTime.now(),
                60,
                new BigDecimal("5000.00"),
                LessonStatus.COMPLETED,
                "homework",
                "feedback"
        );

        // 2. Добавляем урок (это должно вызвать продюсер + публикацию события)
        lessonService.addLesson(lessonDto);

        // 3. Ждем через Awaitility, пока потребитель обработает событие
        waitUntil(() -> {
            var studentReport = studentReportRepository.findByStudent_StudentId(studentId).orElse(null);
            var teacherReport = teacherReportRepository.findByTeacher_TeacherId(teacherId).orElse(null);

            assertThat(studentReport).isNotNull();
            assertThat(studentReport.getLessonsCount()).isEqualTo(1);
            assertThat(studentReport.getLessonsPrice()).isEqualByComparingTo("5000.00");

            assertThat(teacherReport).isNotNull();
            assertThat(teacherReport.getLessonsCount()).isEqualTo(1);
            assertThat(teacherReport.getLessonsPrice()).isEqualByComparingTo("5000.00");
        });
    }
}
