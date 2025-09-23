package org.teacher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.dto.RefreshTokenDto;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.entity.User;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.UserRepository;
import org.teacher.security.jwt.JwtService;
import org.teacher.service.UserService;

import javax.naming.AuthenticationException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        User user = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(user);
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException {
        String refreshToken = refreshTokenDto.refreshToken();
        if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            User user = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user, refreshToken);
        }
        throw new  AuthenticationException("Invalid refresh token");
    }

    @Override
    @Transactional
    public UserRequestDto getUserById(UUID id) throws ChangeSetPersister.NotFoundException {
        return userMapper.toDto(userRepository.findByUserId(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    @Override
    @Transactional
    public UserRequestDto getUserByEmail(String email) throws ChangeSetPersister.NotFoundException {
        return userMapper.toDto(userRepository.findByEmail(email)
                .orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    @Override
    public User addUser(UserRequestDto userDto){
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        return userRepository.findByEmail(userCredentialsDto.email())
                .filter(user -> passwordEncoder.matches(userCredentialsDto.password(), user.getPassword()))
                .orElseThrow(() -> new AuthenticationException("Email or password is incorrect"));
    }

    private User findByEmail(String email) throws AuthenticationException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(String.format("User with email %s not found", email)));
    }
}
