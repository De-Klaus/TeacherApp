package org.teacher.mapper;

import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserRequestDto toDto(User user);
    User toEntity(UserRequestDto userDto);
    UserResponseDto toResponseDto(UserRequestDto user);
    UserResponseDto toResponseDto(User user);
}
