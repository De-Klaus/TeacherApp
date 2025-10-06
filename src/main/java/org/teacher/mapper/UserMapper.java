package org.teacher.mapper;

import org.mapstruct.Mapping;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserRequestDto toDto(User user);
    @Mapping(target = "userId", ignore = true)
    User toEntity(UserRequestDto userDto);
    UserResponseDto toResponseDto(User user);
    UserCredentialsDto toCredentialsDto(UserRequestDto user);
}
