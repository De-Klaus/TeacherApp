package org.teacher.dto;

public record JwtAuthenticationDto(
        String token,
        String refreshToken
) {
}
