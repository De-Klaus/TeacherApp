package org.teacher.kafka.message;

import java.time.LocalDateTime;

public record LessonCreatedEvent(
        Long lessonId,
        Long studentId,
        Long teacherId,
        LocalDateTime lessonTime,
        int reminderMinutesBefore
) {
}

