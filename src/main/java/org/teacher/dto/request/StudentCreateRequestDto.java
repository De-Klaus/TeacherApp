package org.teacher.dto.request;

import java.time.LocalDate;

public record StudentCreateRequestDto(
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
