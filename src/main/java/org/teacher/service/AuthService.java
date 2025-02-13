package org.teacher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.teacher.dto.RegisterRequest;
import org.teacher.exception.UserAlreadyExistsException;
import org.teacher.model.Role;
import org.teacher.model.User;
import org.teacher.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
        log.info("Проверка наличия пользователя с email: {}", request.getEmail());

        // Проверяем, есть ли уже такой пользователь
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже зарегистрирован");
        }

        // Хешируем пароль перед сохранением
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Создаём пользователя
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setRoles(Collections.singleton(Role.USER)); // Назначаем роль по умолчанию

        // Сохраняем пользователя в БД
        userRepository.save(newUser);
        log.info("Пользователь {} зарегистрирован", newUser.getEmail());

        return newUser;
    }
}
