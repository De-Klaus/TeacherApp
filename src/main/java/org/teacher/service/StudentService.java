package org.teacher.service;

import org.teacher.dto.StudentDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    StudentDto addStudent(StudentDto studentDto);
    Optional<StudentDto> getById(Long studentId);
    List<StudentDto> getAll();
    StudentDto update(Long studentId, StudentDto dto);
    void delete(Long studentId);
    BigDecimal calculateBalance(Long studentId);
}
