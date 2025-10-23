package org.teacher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.dto.RefreshTokenDto;
import org.teacher.dto.TeacherDto;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.*;
import org.teacher.exception.DuplicateUserException;
import org.teacher.mapper.UserMapper;
import org.teacher.repository.StudentClaimTokenRepository;
import org.teacher.repository.StudentRepository;
import org.teacher.repository.UserRepository;
import org.teacher.security.jwt.JwtService;
import org.teacher.service.UserService;

import org.springframework.security.core.AuthenticationException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final StudentClaimTokenRepository tokenRepository;

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
        throw new  AuthenticationException("Invalid refresh token") {};
    }

    @Override
    @Transactional
    public Optional<UserResponseDto> getUserById(UUID id) {
        return userRepository.findByUserId(id)
                .map(userMapper::toResponseDto);
    }

    @Override
    @Transactional
    public Optional<UserResponseDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                        .map(userMapper::toResponseDto);
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto addUser(UserRequestDto userDto){
        if (userRepository.existsByEmail(userDto.email())) {
            throw new DuplicateUserException("User with email already exists: " + userDto.email());
        }
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto registerStudent(UUID claimToken, UserRequestDto userDto) throws AuthenticationException{
        StudentClaimToken token = tokenRepository.findByToken(claimToken)
                .orElseThrow(() -> new AuthenticationException("Invalid claim token") {});

        if (token.isUsed()) {
            throw new AuthenticationException("Token already used") {};
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Token expired") {};
        }

        if (userRepository.existsByEmail(userDto.email())) {
            throw new DuplicateUserException("User with email already exists: " + userDto.email());
        }

        Student student = token.getStudent();

        User user = token.getUser();
        if(!Objects.equals(userDto.firstName(), user.getFirstName())) {
            user.setFirstName(userDto.firstName());
        }
        if(!Objects.equals(userDto.lastName(), user.getLastName())) {
            user.setLastName(userDto.lastName());
        }
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(Role.STUDENT);
        var saveUser = userRepository.save(user);

        token.setUsed(true);
        tokenRepository.save(token);

        student.setStatus(StudentStatus.ACTIVE);
        studentRepository.save(student);

        return userMapper.toResponseDto(saveUser);
    }

    @Override
    public List<UserResponseDto> getAllWithoutTeacher() {
        return userRepository.findTeachersWithoutTeacherEntity(Role.USER)
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        return userRepository.findByEmail(userCredentialsDto.email())
                .filter(user -> passwordEncoder.matches(userCredentialsDto.password(), user.getPassword()))
                .orElseThrow(() -> new AuthenticationException("Email or password is incorrect"){});
    }

    private User findByEmail(String email) throws AuthenticationException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(String.format("User with email %s not found", email)){});
    }
}
