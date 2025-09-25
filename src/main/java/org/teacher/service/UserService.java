package org.teacher.service;

import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.dto.RefreshTokenDto;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;

import javax.naming.AuthenticationException;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException;

    Optional<UserResponseDto> getUserById(UUID id);

    Optional<UserResponseDto> getUserByEmail(String email);

    UserResponseDto addUser(UserRequestDto user);
}
