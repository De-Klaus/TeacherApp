package org.teacher.dto;

import org.teacher.entity.StudentTeacherStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StudentTeacherDto(
                 Long id,
                 Long studentId,
                 Long teacherId,
                 LocalDate startDate,
                 LocalDate endDate,
                 BigDecimal agreedRate,
                 StudentTeacherStatus status
) {
}
