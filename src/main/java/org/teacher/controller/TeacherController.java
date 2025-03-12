package org.teacher.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.repository.UserRepository;
import org.teacher.service.JwtService;
import org.teacher.model.User;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public TeacherController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getStudents(@RequestHeader("Authorization") String token) {
        Long teacherId = jwtService.extractTeacherId(token.substring(7)); // 👈 Извлекаем teacherId из токена
        if (teacherId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещён");
        }
        List<User> students = userRepository.findByTeacherId(teacherId);
        return ResponseEntity.ok(students);
    }
}
