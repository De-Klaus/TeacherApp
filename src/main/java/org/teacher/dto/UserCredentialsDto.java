package org.teacher.dto;

import java.util.UUID;

public record UserCredentialsDto(
        String email,
        String password,
        UUID claimToken
) {
}
