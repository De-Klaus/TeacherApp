package org.teacher.dto.request;

import org.teacher.entity.StudentTeacherStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StudentTeacherSystemRequestDto(
        Long teacherId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal agreedRate,
        StudentTeacherStatus status,
        StudentCreateRequestDto student
) {
}
