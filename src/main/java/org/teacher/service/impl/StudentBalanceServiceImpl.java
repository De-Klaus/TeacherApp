package org.teacher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teacher.entity.Lesson;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Payment;
import org.teacher.entity.Student;
import org.teacher.service.StudentBalanceService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentBalanceServiceImpl implements StudentBalanceService {
    @Override
    public BigDecimal calculateBalance(Student student, List<Payment> payments) {
        // сумма всех завершённых уроков
        BigDecimal totalLessons = student.getLessons().stream()
                .filter(l -> l.getStatus() == LessonStatus.COMPLETED)
                .map(Lesson::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // сумма всех платежей
        BigDecimal totalPayments = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // баланс = оплачено − сумма уроков
        return totalPayments.subtract(totalLessons);
    }
}
