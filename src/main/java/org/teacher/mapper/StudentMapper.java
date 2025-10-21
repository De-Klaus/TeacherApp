package org.teacher.mapper;

import org.teacher.dto.StudentDto;
import org.teacher.dto.request.StudentCreateRequestDto;
import org.teacher.entity.Student;

public interface StudentMapper {
    StudentDto toDto(Student student);
    Student toEntity(StudentDto studentDto);
    Student toStudent(StudentCreateRequestDto dto);
}
