package org.teacher.mapper;

import org.teacher.dto.LessonStatusDto;
import org.teacher.entity.LessonStatus;

public interface LessonStatusMapper {
    LessonStatusDto toDto(LessonStatus status);
    LessonStatus toEntity(LessonStatusDto  lsDto);
}
