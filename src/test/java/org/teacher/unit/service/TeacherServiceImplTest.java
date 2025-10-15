package org.teacher.unit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.dto.StudentDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.entity.*;
import org.teacher.repository.StudentRepository;
import org.teacher.repository.StudentTeacherRepository;
import org.teacher.repository.TeacherRepository;
import org.teacher.repository.UserRepository;
import org.teacher.service.TeacherService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
@Transactional
class TeacherServiceImplTest {

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

    private Teacher teacher;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        User teacherUser = new User();
        teacherUser.setRoles(Set.of(Role.TEACHER));
        var saveUserT = userRepository.save(teacherUser);

        // Создаем учителя
        teacher = Teacher.builder()
                .teacherId(1L)
                .user(saveUserT)
                .subject("Math")
                .timeZone("UTC")
                .build();

        teacherRepository.save(teacher);

        User student1User = new User();
        student1User.setRoles(Set.of(Role.STUDENT));
        var saveStudent1User = userRepository.save(student1User);

        User student2User = new User();
        student2User.setRoles(Set.of(Role.STUDENT));
        var saveStudent2User = userRepository.save(student2User);

        // Создаем учеников
        student1 = Student.builder().user(saveStudent1User).grade(5).timeZone("UTC").build();
        student2 = Student.builder().user(saveStudent2User).grade(6).timeZone("UTC").build();
        studentRepository.saveAll(List.of(student1, student2));

        // Привязываем студентов к учителю
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
    }

    @Test
    void shouldReturnAllStudentsForTeacher() {
        List<StudentDto> students = teacherService.getStudentsByTeacherId(teacher.getTeacherId());

        assertNotNull(students);
        assertEquals(2, students.size());

        List<Long> studentIds = students.stream().map(StudentDto::id).toList();
        assertTrue(studentIds.contains(student1.getStudentId()));
        assertTrue(studentIds.contains(student2.getStudentId()));
    }

}