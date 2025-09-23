package org.teacher.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.dto.RefreshTokenDto;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.UserRequestDto;

import javax.naming.AuthenticationException;
import java.util.UUID;

public interface UserService {

    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException;

    UserRequestDto getUserById(UUID id) throws ChangeSetPersister.NotFoundException;

    UserRequestDto getUserByEmail(String email) throws ChangeSetPersister.NotFoundException;

    UUID addUser(UserRequestDto user);
}
