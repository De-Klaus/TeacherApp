package org.teacher.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserRequestDto(
        String firstName,
        String lastName,
        @NotBlank @Email String email,
        @NotBlank String password) {
}
