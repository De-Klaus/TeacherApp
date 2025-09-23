package org.teacher.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.entity.User;
import org.teacher.mapper.UserMapper;
import org.teacher.service.UserService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userDto) {
        User user = userService.addUser(userDto);
        UserResponseDto responseDto = userMapper.toResponseDto(user);
        return ResponseEntity
                .created(URI.create("/users/" + user.getUserId()))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        try {
            UserRequestDto userDto = userService.getUserById(id);
            UserResponseDto responseDto = userMapper.toResponseDto(userDto);
            return ResponseEntity.ok(responseDto);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) throws ChangeSetPersister.NotFoundException {
        try {
            UserRequestDto userDto = userService.getUserByEmail(email);
            UserResponseDto responseDto = userMapper.toResponseDto(userDto);
            return ResponseEntity.ok(responseDto);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}
