package org.teacher.kafka.message;

import java.math.BigDecimal;

public record LessonCompletedEvent(
        Long lessonId,
        Long studentId,
        Long teacherId,
        BigDecimal price,
        Integer durationMinutes
) {
}
