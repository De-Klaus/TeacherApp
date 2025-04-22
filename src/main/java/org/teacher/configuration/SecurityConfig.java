package org.teacher.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.teacher.component.JwtAuthenticationFilter;
import org.teacher.service.JwtService;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean //возвращаем кастомный MyUserDetailsService, который напишем далее
    public UserDetailsService userDetailsService(){
        return new MyUserDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return  http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Разрешаем CORS
                .csrf(AbstractHttpConfigurer::disable) // ✅ Отключаем CSRF (если используешь JWT)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/users","teacher/welcome").permitAll() // Вход без авторизации
                        .requestMatchers("/teacher/**").hasRole("TEACHER") // 👈 Доступ к /teacher только для учителей
                        .requestMatchers("/students", "/students/create", "/students/{id}").hasAnyRole("TEACHER", "ADMIN")  // ✅ Требуем авторизацию для /students
                        .requestMatchers("teacher/**").hasAnyRole("TEACHER", "ADMIN") // Доступ только для авторизованных

                        // Spring Data REST endpoints
                        .requestMatchers(HttpMethod.GET, "/students/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/students/**").hasRole("ADMIN")

                        .anyRequest().authenticated() // Закрываем все остальные пути
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ Если используешь JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 🔥 Добавляем фильтр JWT
                .formLogin(AbstractAuthenticationFilterConfigurer::disable) // ✅ Отключаем форму логина
                //.logout(AbstractHttpConfigurer::disable) // ✅ Отключаем логаут
                .logout(logout -> logout.logoutUrl("/auth/logout").permitAll()) // ✅ Возможность логаута, но без формы входа
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:10000",  // Фронт в деве
                "https://teacher-app-frontend-mnsu.onrender.com" // Фронт в проде
        )); // ✅ Разрешаем фронт
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Разрешаем нужные методы
        //configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // ✅ Разрешаем заголовки
        configuration.setAllowedHeaders(List.of("*")); // ✅ Разрешаем все заголовки
        configuration.setAllowCredentials(true); // ✅ Разрешаем отправку cookies и токенов

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean //Ставим степень кодировки, с которой кодировали пароль в базе
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(5);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }


}

