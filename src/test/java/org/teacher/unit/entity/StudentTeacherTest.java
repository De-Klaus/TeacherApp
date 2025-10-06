package org.teacher.unit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teacher.entity.Student;
import org.teacher.entity.StudentTeacher;
import org.teacher.entity.StudentTeacherStatus;
import org.teacher.entity.Teacher;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class StudentTeacherTest {

    private Student student;
    private Teacher teacher;
    private StudentTeacher studentTeacher;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setStudentId(1L);

        teacher = new Teacher();
        teacher.setTeacherId(1L);

        studentTeacher = new StudentTeacher();
        studentTeacher.setStudent(student);
        studentTeacher.setTeacher(teacher);
        studentTeacher.setAgreedRate(new BigDecimal("30"));
        studentTeacher.setStatus(StudentTeacherStatus.ACTIVE);
    }

    @Test
    void testStudentTeacherFields() {
        assertThat(studentTeacher.getStudent()).isEqualTo(student);
        assertThat(studentTeacher.getTeacher()).isEqualTo(teacher);
        assertThat(studentTeacher.getAgreedRate()).isEqualByComparingTo(new BigDecimal("30"));
        assertThat(studentTeacher.getStatus()).isEqualTo(StudentTeacherStatus.ACTIVE);

        // Проверяем смену статуса
        studentTeacher.setStatus(StudentTeacherStatus.PAUSED);
        assertThat(studentTeacher.getStatus()).isEqualTo(StudentTeacherStatus.PAUSED);
    }
}