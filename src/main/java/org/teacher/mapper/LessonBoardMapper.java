package org.teacher.mapper;

import org.teacher.dto.LessonBoardDto;
import org.teacher.entity.LessonBoard;

public interface LessonBoardMapper {

    LessonBoardDto toDto(LessonBoard lessonBoard);
    LessonBoard toEntity(LessonBoardDto lessonBoardDto);
}
