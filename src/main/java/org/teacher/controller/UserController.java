package org.teacher.controller;

import org.springframework.http.ResponseEntity;
import lombok.*;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.UserDTO;
import org.teacher.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    /*@PostMapping("/users")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user.getEmail(), user.getPassword()));
    }*/
}
