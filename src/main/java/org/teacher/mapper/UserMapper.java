package org.teacher.mapper;

import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.StudentCreateRequestDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.User;

public interface UserMapper {
    User toEntity(UserRequestDto userDto);
    User toEntity(UserResponseDto userDto);
    User toUserEntity(StudentCreateRequestDto dto);
    UserRequestDto toDto(User user);
    UserResponseDto toResponseDto(User user);
    UserCredentialsDto toCredentialsDto(UserRequestDto user);
}
