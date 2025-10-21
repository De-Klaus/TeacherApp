package org.teacher.dto;

import org.teacher.entity.StudentStatus;

import java.time.LocalDate;
import java.util.UUID;

public record StudentDto(
         Long id,
         StudentStatus status,
         UUID userId,
         String firstName,
         String lastName,
         LocalDate birthDate,
         String phoneNumber,
         String city,
         String timeZone,
         Integer grade,
         String school
) {
}
