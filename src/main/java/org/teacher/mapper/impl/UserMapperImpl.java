package org.teacher.mapper.impl;

import org.springframework.stereotype.Component;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.StudentCreateRequestDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.Role;
import org.teacher.entity.User;
import org.teacher.mapper.UserMapper;

import java.util.Collections;
import java.util.Set;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toEntity(UserRequestDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setRoles(userDto.roles() != null ? userDto.roles() : Collections.emptySet());

        return user;
    }

    @Override
    public User toEntity(UserResponseDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setRoles(userDto.roles() != null ? userDto.roles() : Collections.emptySet());
        return user;
    }

    @Override
    public User toUserEntity(StudentCreateRequestDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setRoles(Collections.emptySet());
        return user;
    }

    @Override
    public UserRequestDto toDto(User user) {
        if (user == null) return null;

        Set<Role> roles = user.getRoles() != null ? user.getRoles() : Collections.emptySet();

        return new UserRequestDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                roles
        );
    }

    @Override
    public UserResponseDto toResponseDto(User user) {
        if (user == null) return null;

        Set<Role> roles = user.getRoles() != null ? user.getRoles() : Collections.emptySet();

        return new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles
        );
    }

    @Override
    public UserCredentialsDto toCredentialsDto(UserRequestDto user) {
        if (user == null) return null;

        return new UserCredentialsDto(
                user.email(),
                user.password(),
                null
        );
    }
}
