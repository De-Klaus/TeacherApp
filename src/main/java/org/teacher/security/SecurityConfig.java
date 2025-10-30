package org.teacher.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.teacher.security.jwt.JwtFilter;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Разрешаем CORS
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register-by-token/**").permitAll()
                        .requestMatchers("/ws-board/**").permitAll()
                        .requestMatchers("/users/**").authenticated()
                        .requestMatchers("/**").authenticated()
                        .anyRequest().denyAll()
                )
                .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",   // Фронт в тесте
                "http://localhost:10000",  // Фронт в деве
                "https://teacher-app-frontend-mnsu.onrender.com" // Фронт в проде
        )); // ✅ Разрешаем фронт
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",      // Любой порт localhost
                "http://192.168.*.*:*",    // Локальная сеть (для React Native/Expo)
                "http://10.*.*.*:*"        // Альтернативная локальная сеть
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // ✅ Разрешаем нужные методы
        //configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // ✅ Разрешаем заголовки
        configuration.setAllowedHeaders(List.of("*")); // ✅ Разрешаем все заголовки
        configuration.setAllowCredentials(true); // ✅ Разрешаем отправку cookies и токенов

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        // optionally be explicit:
        // source.registerCorsConfiguration("/ws-board/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
