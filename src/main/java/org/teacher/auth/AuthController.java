package org.teacher.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.teacher.dto.AuthRequest;
import org.teacher.dto.AuthResponse;
import org.teacher.service.JwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        logger.info("Попытка входа пользователя: {}", request.getUsername());

        try {
            // Аутентифицируем пользователя (Spring Security проверит логин и пароль)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Загружаем UserDetails (Spring Security сам кэширует его)
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // Генерируем JWT-токен
            String jwtToken = jwtService.generateToken(userDetails);

            logger.info("Пользователь {} успешно аутентифицирован. Токен сгенерирован.", request.getUsername());

            // Возвращаем токен клиенту
            return ResponseEntity.ok(new AuthResponse(jwtToken));

        } catch (Exception e) {
            logger.error("Ошибка аутентификации пользователя {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(401).body("Ошибка авторизации");
        }
    }

    /*private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request.getName(), request.getEmail(), request.getPassword());
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return userService.login(request.getEmail(), request.getPassword());
    }*/
}
