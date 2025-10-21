package org.teacher.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record StudentClaimTokenDto(
        Long studentId,
        UUID claimToken,
        LocalDateTime expiresAt
) {
}
