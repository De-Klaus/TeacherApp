package org.teacher.service;


import org.teacher.dto.TeacherDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherService {
    TeacherDto addTeacher(TeacherDto teacherDto);
    Optional<TeacherDto> getById(Long teacherId);
    Optional<TeacherDto> findByUserId(UUID userId);
    List<TeacherDto> getAll();
    TeacherDto update(Long teacherId, TeacherDto dto);
    void delete(Long teacherId);
}
