package org.teacher.controller;

import org.springframework.http.ResponseEntity;
import lombok.*;
import org.springframework.web.bind.annotation.*;
import org.teacher.model.User;
import org.teacher.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user.getEmail(), user.getPassword()));
    }
}
