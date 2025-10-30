package org.teacher.dto;

import org.springframework.lang.NonNull;

public record UserCredentialsDto(
        String email,
        String password
) {
    @Override
    public @NonNull String toString() {
        return "UserCredentialsDto[email=%s, password=****]".formatted(email);
    }
}
