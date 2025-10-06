package org.teacher.dto;

import org.teacher.entity.Lesson;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record StudentDto(
         Long studentId,
         UUID userId,
         LocalDate birthDate,
         String phoneNumber,
         String city,
         String timeZone,
         Integer grade,
         String school,
         List<Lesson> lessons
) {
}
