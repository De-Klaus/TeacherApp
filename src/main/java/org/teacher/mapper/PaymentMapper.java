package org.teacher.mapper;

import org.mapstruct.*;
import org.teacher.dto.PaymentDto;
import org.teacher.entity.Payment;
import org.teacher.entity.Student;
import org.teacher.entity.User;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {
    @Mapping(source = "student.studentId", target = "studentId")
    PaymentDto toDto(Payment payment);
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "student", source = "studentId", qualifiedByName = "mapStudentIdToStudent")
    Payment toEntity(PaymentDto paymentDto);

    @Named("mapStudentIdToStudent")
    default Student mapStudentIdToStudent(Long studentId) {
        if (studentId == null) return null;
        return Student.builder()
                .studentId(studentId)
                .build();
    }
}
