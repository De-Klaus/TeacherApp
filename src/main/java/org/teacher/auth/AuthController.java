package org.teacher.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.teacher.dto.AuthRequest;
import org.teacher.dto.AuthResponse;
import org.teacher.dto.RegisterRequest;
import org.teacher.exception.UserAlreadyExistsException;
import org.teacher.model.User;
import org.teacher.repository.UserRepository;
import org.teacher.service.AuthService;
import org.teacher.service.JwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        logger.info("Попытка входа пользователя: {}", request.getUsername());

        try {
            // Аутентифицируем пользователя (Spring Security проверит логин и пароль)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Загружаем UserDetails (Spring Security сам кэширует его)
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));;

            // Генерируем JWT-токен
            String jwtToken = jwtService.generateToken(user);

            logger.info("Пользователь {} успешно аутентифицирован. Токен сгенерирован.", request.getUsername());

            // Возвращаем токен клиенту
            return ResponseEntity.ok(new AuthResponse(jwtToken));

        } catch (Exception e) {
            logger.error("Ошибка аутентификации пользователя {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(401).body("Ошибка авторизации");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("Регистрация нового пользователя: {}", request.getEmail());

        try {
            User newUser = authService.registerUser(request);
            String jwtToken = jwtService.generateToken(newUser);
            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (UserAlreadyExistsException e) {
            logger.error("Ошибка регистрации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
