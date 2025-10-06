package org.teacher.service;

import org.teacher.dto.PaymentDto;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    PaymentDto addPayment(PaymentDto paymentDto);
    Optional<PaymentDto> getById(Long paymentId);
    List<PaymentDto> getAll();
    PaymentDto update(Long paymentId, PaymentDto dto);
    void delete(Long paymentId);
}
