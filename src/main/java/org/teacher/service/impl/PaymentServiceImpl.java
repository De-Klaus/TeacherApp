package org.teacher.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teacher.dto.PaymentDto;
import org.teacher.entity.Payment;
import org.teacher.mapper.PaymentMapper;
import org.teacher.repository.PaymentRepository;
import org.teacher.service.PaymentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDto addPayment(PaymentDto paymentDto) {
        Payment payment = paymentMapper.toEntity(paymentDto);
        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDto(saved);
    }

    @Override
    public Optional<PaymentDto> getById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(paymentMapper::toDto);
    }

    @Override
    public List<PaymentDto> getAll() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public PaymentDto update(Long paymentId, PaymentDto dto) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + paymentId));

        payment.setPaymentDate(dto.paymentDate());
        payment.setAmount(dto.amount());
        payment.setMethod(dto.method());
        payment.setTransactionId(dto.transactionId());

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toDto(saved);
    }

    @Override
    public void delete(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
