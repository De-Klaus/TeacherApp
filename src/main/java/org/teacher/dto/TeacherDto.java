package org.teacher.dto;

import java.util.UUID;

public record TeacherDto(
                Long id,
                UUID userId,
                String firstName,
                String lastName,
                String subject,
                String timeZone
) {
}
