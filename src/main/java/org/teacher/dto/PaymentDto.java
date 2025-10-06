package org.teacher.dto;

import org.teacher.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
            Long paymentId,
            Long studentId,
            LocalDateTime paymentDate,
            BigDecimal amount,
            PaymentMethod method,
            String transactionId
) {
}
