package org.teacher.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserRequestDto(
        UUID userId,
        String firstName,
        String lastName,
        @NotBlank @Email String email,
        @NotBlank String password) {
}
