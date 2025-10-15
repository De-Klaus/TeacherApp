package org.teacher.mapper.impl;

import org.springframework.stereotype.Component;
import org.teacher.dto.StudentTeacherDto;
import org.teacher.entity.Student;
import org.teacher.entity.StudentTeacher;
import org.teacher.entity.Teacher;
import org.teacher.mapper.StudentTeacherMapper;

@Component
public class StudentTeacherMapperImpl implements StudentTeacherMapper {

    @Override
    public StudentTeacherDto toDto(StudentTeacher studentTeacher) {
        if (studentTeacher == null) return null;
        Long studentId = studentTeacher.getStudent() != null ? studentTeacher.getStudent().getStudentId() : null;
        Long teacherId = studentTeacher.getTeacher() != null ? studentTeacher.getTeacher().getTeacherId() : null;
        return new StudentTeacherDto(
                                studentTeacher.getStudentTeacherId(),
                                studentId,
                                teacherId,
                                studentTeacher.getStartDate(),
                                studentTeacher.getEndDate(),
                                studentTeacher.getAgreedRate(),
                                studentTeacher.getStatus()
        );
    }

    @Override
    public StudentTeacher toEntity(StudentTeacherDto studentTeacherDto) {
        if (studentTeacherDto == null) return null;

        Student student = studentTeacherDto.studentId() != null ? Student.builder().studentId(studentTeacherDto.studentId()).build() : null;
        Teacher teacher = studentTeacherDto.teacherId() != null ? Teacher.builder().teacherId(studentTeacherDto.teacherId()).build() : null;

        StudentTeacher studentTeacher = new StudentTeacher();
        studentTeacher.setStudentTeacherId(studentTeacherDto.id());
        studentTeacher.setStudent(student);
        studentTeacher.setTeacher(teacher);
        studentTeacher.setStartDate(studentTeacherDto.startDate());
        studentTeacher.setEndDate(studentTeacherDto.endDate());
        studentTeacher.setAgreedRate(studentTeacherDto.agreedRate());
        studentTeacher.setStatus(studentTeacherDto.status());
        return studentTeacher;
    }
}
