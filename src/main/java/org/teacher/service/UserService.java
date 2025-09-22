package org.teacher.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.dto.RefreshTokenDto;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.UserDto;

import javax.naming.AuthenticationException;

public interface UserService {

    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception;

    UserDto getUserById(String id) throws ChangeSetPersister.NotFoundException;

    UserDto getUserByEmail(String email) throws ChangeSetPersister.NotFoundException;

    String addUser(UserDto user);
}
