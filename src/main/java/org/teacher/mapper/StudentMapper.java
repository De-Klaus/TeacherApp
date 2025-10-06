package org.teacher.mapper;

import org.mapstruct.*;
import org.teacher.dto.StudentDto;
import org.teacher.entity.Student;
import org.teacher.entity.User;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
    @Mapping(source = "user.userId", target = "userId")
    StudentDto toDto(Student student);
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUserIdToUser")
    Student toEntity(StudentDto studentDto);

    @Named("mapUserIdToUser")
    default User mapUserIdToUser(UUID userId) {
        if (userId == null) return null;
        return User.builder()
                .userId(userId)
                .build();
    }
}
