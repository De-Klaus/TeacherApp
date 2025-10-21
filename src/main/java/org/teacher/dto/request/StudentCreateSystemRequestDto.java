package org.teacher.dto.request;

public record StudentCreateSystemRequestDto(
        Long id,
        String phoneNumber,
        String city,
        String timeZone,
        Integer grade,
        String school
) {
}
