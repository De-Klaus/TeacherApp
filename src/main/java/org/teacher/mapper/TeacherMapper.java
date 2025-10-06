package org.teacher.mapper;

import org.mapstruct.*;
import org.teacher.dto.TeacherDto;
import org.teacher.entity.Teacher;
import org.teacher.entity.User;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {
    @Mapping(source = "user.userId", target = "userId")
    TeacherDto toDto(Teacher teacher);
    @Mapping(target = "teacherId", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUserIdToUser")
    Teacher toEntity(TeacherDto teacherDto);

    @Named("mapUserIdToUser")
    default User mapUserIdToUser(UUID userId) {
        if (userId == null) return null;
        return User.builder()
                .userId(userId)
                .build();
    }
}
