package org.teacher.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.teacher.dto.LessonBoardDto;
import org.teacher.entity.Lesson;
import org.teacher.entity.LessonBoard;
import org.teacher.mapper.LessonBoardMapper;

@Component
@RequiredArgsConstructor
public class LessonBoardMapperImpl implements LessonBoardMapper {
    @Override
    public LessonBoardDto toDto(LessonBoard lessonBoard) {
        if (lessonBoard == null) return null;
        Long lessonId = lessonBoard.getLesson() != null ? lessonBoard.getLesson().getLessonId() : null;
        return new LessonBoardDto(
                lessonBoard.getLessonBoardId(),
                lessonId,
                lessonBoard.getSceneJson(),
                lessonBoard.getUpdatedAt()
        );
    }

    @Override
    public LessonBoard toEntity(LessonBoardDto lessonBoardDto) {
        if (lessonBoardDto == null) return null;
        Lesson lesson = lessonBoardDto.lessonId() != null ? Lesson.builder().lessonId(lessonBoardDto.lessonId()).build() : null;
        return LessonBoard.builder()
                .lessonBoardId(lessonBoardDto.id())
                .lesson(lesson)
                .sceneJson(lessonBoardDto.sceneJson())
                .updatedAt(lessonBoardDto.updatedAt())
                .build();
    }
}
