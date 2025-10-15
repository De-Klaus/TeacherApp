package org.teacher.dto;

import java.time.LocalDate;
import java.util.UUID;

public record StudentDto(
         Long id,
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
