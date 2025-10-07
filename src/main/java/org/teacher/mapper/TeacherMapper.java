package org.teacher.mapper;

import org.teacher.dto.TeacherDto;
import org.teacher.entity.Teacher;

public interface TeacherMapper {
    TeacherDto toDto(Teacher teacher);
    Teacher toEntity(TeacherDto teacherDto);
}
