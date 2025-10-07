package org.teacher.mapper;

import org.teacher.dto.PaymentDto;
import org.teacher.entity.*;

public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
    Payment toEntity(PaymentDto paymentDto);
}
