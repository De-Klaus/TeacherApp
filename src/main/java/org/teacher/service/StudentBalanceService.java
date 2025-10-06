package org.teacher.service;

import org.teacher.entity.Payment;
import org.teacher.entity.Student;

import java.math.BigDecimal;
import java.util.List;

public interface StudentBalanceService {
    BigDecimal calculateBalance(Student student, List<Payment> payment);
}
