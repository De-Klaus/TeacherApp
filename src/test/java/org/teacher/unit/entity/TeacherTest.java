package org.teacher.unit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teacher.entity.Lesson;
import org.teacher.entity.Student;
import org.teacher.entity.StudentTeacher;
import org.teacher.entity.Teacher;


import static org.assertj.core.api.Assertions.assertThat;

class TeacherTest {

    private Teacher teacher1;
    private Teacher teacher2;
    private Lesson lesson;
    private StudentTeacher studentTeacher;

    @BeforeEach
    void setUp() {
        teacher1 = Teacher.builder()
                .teacherId(1L)
                .subject("Math")
                .timeZone("GMT+7")
                .build();

        teacher2 = Teacher.builder()
                .teacherId(teacher1.getTeacherId()) // одинаковый UUID для equals/hashCode
                .subject("Math")
                .timeZone("GMT+7")
                .build();

        // Создаем студент для связки
        Student student = Student.builder()
                .studentId(1L)
                .city("Hanoi")
                .timeZone("GMT+7")
                .build();

        // Создаем урок
        lesson = Lesson.builder()
                .lessonId(1L)
                .teacher(teacher1)
                .student(student)
                .build();

        // Создаем StudentTeacher
        studentTeacher = StudentTeacher.builder()
                .studentTeacherId(1L)
                .student(student)
                .teacher(teacher1)
                .agreedRate(new java.math.BigDecimal("25"))
                .build();
    }

    @Test
    void testEqualsAndHashCode() {
        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());

        teacher2.setSubject("English");
        assertThat(teacher1).isEqualTo(teacher2);
    }

    @Test
    void testAddLesson() {
        // добавляем урок в список teacher
        teacher1.getLessons().add(lesson);

        assertThat(teacher1.getLessons())
                .hasSize(1)
                .contains(lesson);

        // проверяем связь с StudentTeacher
        teacher1.getStudents().add(studentTeacher);
        assertThat(teacher1.getStudents())
                .hasSize(1)
                .contains(studentTeacher);
    }

    @Test
    void testSubjectField() {
        assertThat(teacher1.getSubject()).isEqualTo("Math");

        teacher1.setSubject("Physics");
        assertThat(teacher1.getSubject()).isEqualTo("Physics");
    }
}