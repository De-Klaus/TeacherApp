package org.teacher.dto;

import org.teacher.entity.LessonStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LessonDto(
                        Long lessonId,
                        Long studentId,
                        Long teacherId,
                        LocalDateTime scheduledAt,
                        Integer durationMinutes,
                        BigDecimal price,
                        LessonStatus status,
                        String homework,
                        String feedback
) {
}
