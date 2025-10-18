package org.teacher.dto;

import java.time.LocalDateTime;

public record LessonBoardDto(
        Long id,
        Long lessonId,
        String sceneJson,
        LocalDateTime updatedAt
        ) {
}
