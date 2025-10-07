package org.teacher.mapper;

import org.teacher.dto.StudentTeacherDto;
import org.teacher.entity.StudentTeacher;

public interface StudentTeacherMapper {
    StudentTeacherDto toDto(StudentTeacher studentTeacher);
    StudentTeacher toEntity(StudentTeacherDto studentTeacherDto);
}
