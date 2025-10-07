package org.teacher.mapper;

import org.teacher.dto.LessonDto;
import org.teacher.entity.Lesson;

public interface LessonMapper {
    LessonDto toDto(Lesson lesson);
    Lesson toEntity(LessonDto lessonDto);
}
