package org.teacher.unit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teacher.entity.*;


import static org.assertj.core.api.Assertions.assertThat;

class StudentTest {

    private Student student1;
    private Student student2;
    private Lesson lesson;
    private StudentTeacher studentTeacher;

    @BeforeEach
    void setUp() {
        student1 = Student.builder()
                .studentId(1L)
                .grade(10)
                .city("Hanoi")
                .timeZone("GMT+7")
                .build();

        student2 = Student.builder()
                .studentId(student1.getStudentId())
                .grade(10)
                .city("Hanoi")
                .timeZone("GMT+7")
                .build();

        Teacher teacher = Teacher.builder()
                .teacherId(1L)
                .timeZone("GMT+7")
                .build();

        lesson = new Lesson();
        lesson.setLessonId(1L);
        lesson.setStudent(student1);
        lesson.setTeacher(teacher);

        studentTeacher = new StudentTeacher();
        studentTeacher.setStudentTeacherId(1L);
        studentTeacher.setStudent(student1);
        studentTeacher.setTeacher(teacher);
        studentTeacher.setAgreedRate(new java.math.BigDecimal("30"));
        studentTeacher.setStatus(StudentTeacherStatus.ACTIVE);
    }

    @Test
    void testEqualsAndHashCode() {
        // Проверяем, что два объекта с одинаковым userId и email равны
        assertThat(student1).isEqualTo(student2);
        assertThat(student1.hashCode()).isEqualTo(student2.hashCode());

        // Меняем email → объекты не равны
        student2.setCity("Moscow");
        assertThat(student1).isEqualTo(student2);
    }

    @Test
    void testAddLessonAndTeacher() {
        student1.getLessons().add(lesson);
        student1.getTeachers().add(studentTeacher);

        assertThat(student1.getLessons()).contains(lesson);
        assertThat(student1.getTeachers()).contains(studentTeacher);
    }

    @Test
    void testFields() {
        assertThat(student1.getGrade()).isEqualTo(10);
        assertThat(student1.getCity()).isEqualTo("Hanoi");
        assertThat(student1.getTimeZone()).isEqualTo("GMT+7");
    }

}