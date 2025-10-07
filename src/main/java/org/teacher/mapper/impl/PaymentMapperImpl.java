package org.teacher.mapper.impl;

import org.springframework.stereotype.Component;
import org.teacher.dto.PaymentDto;
import org.teacher.entity.Payment;
import org.teacher.entity.Student;
import org.teacher.mapper.PaymentMapper;

@Component
public class PaymentMapperImpl implements PaymentMapper {
    @Override
    public PaymentDto toDto(Payment payment) {
        if (payment == null) return null;
        Long studentId = payment.getStudent() != null ? payment.getStudent().getStudentId() : null;
        return new PaymentDto(
                payment.getPaymentId(),
                studentId,
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getTransactionId()
        );
    }

    @Override
    public Payment toEntity(PaymentDto paymentDto) {
        if (paymentDto == null) return null;

        Student student = paymentDto.studentId() != null ? Student.builder().studentId(paymentDto.studentId()).build() : null;

        Payment payment = new Payment();
        payment.setPaymentId(paymentDto.paymentId());
        payment.setStudent(student);
        payment.setPaymentDate(paymentDto.paymentDate());
        payment.setAmount(paymentDto.amount());
        payment.setMethod(paymentDto.method());
        payment.setTransactionId(paymentDto.transactionId());
        return payment;
    }
}
