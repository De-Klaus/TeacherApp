package org.teacher.service;


import org.teacher.dto.StudentTeacherDto;
import org.teacher.entity.StudentTeacher;

import java.util.List;
import java.util.Optional;

public interface StudentTeacherService {
    StudentTeacherDto addStudentTeacher(StudentTeacherDto studentTeacherDto);
    Optional<StudentTeacherDto> getById(Long studentTeacherId);
    List<StudentTeacherDto> getAll();
    StudentTeacherDto update(Long studentTeacherId, StudentTeacherDto dto);
    void delete(Long studentTeacherId);
    Optional<StudentTeacher> findActiveByStudentAndTeacher(Long studentId, Long teacherId);
    StudentTeacherDto endStudentTeacher(Long studentTeacherId);
    Optional<StudentTeacher> findActiveByStudent(Long studentId);
}
