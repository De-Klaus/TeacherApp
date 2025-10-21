package org.teacher.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LessonDto(
                        Long id,
                        @NotNull(message = "Студент обязателен")
                        Long studentId,
                        @NotNull(message = "Преподаватель обязателен")
                        Long teacherId,
                        LocalDateTime scheduledAt,
                        Integer durationMinutes,
                        BigDecimal price,
                        LessonStatusDto status,
                        String topic,
                        String homework,
                        String feedback
) {
}
