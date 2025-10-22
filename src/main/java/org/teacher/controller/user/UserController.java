package org.teacher.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.TeacherDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userDto) {
        UserResponseDto responseDto = userService.addUser(userDto);
        return ResponseEntity
                .created(URI.create("/users/" + responseDto.userId()))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @RequestParam
            @NotBlank(message = "Email is required")
            @Email(message = "Invalid email format") String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/without-teacher")
    public ResponseEntity<List<UserResponseDto>> getAllUserWithoutTeacherEntity() {
        return ResponseEntity.ok(userService.getAllWithoutTeacher());
    }
}
