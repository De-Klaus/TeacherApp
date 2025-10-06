package org.teacher.dto;

import org.teacher.entity.Lesson;

import java.util.List;
import java.util.UUID;

public record TeacherDto(
                Long teacherId,
                UUID userId,
                String subject,
                String timeZone,
                List<Lesson> lessons
) {
}
