package org.teacher.mapper;

import org.teacher.dto.StudentDto;
import org.teacher.entity.Student;

public interface StudentMapper {
    StudentDto toDto(Student student);
    Student toEntity(StudentDto studentDto);
}
