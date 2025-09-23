package org.teacher.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.dto.response.UserResponseDto;
import org.teacher.service.UserService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userDto) {
        UUID id = userService.addUser(userDto);
        UserResponseDto responseDto = new UserResponseDto(
                id,
                userDto.email(),
                userDto.firstName(),
                userDto.lastName()
        );
        return ResponseEntity
                .created(URI.create("/users/" + id))
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) throws ChangeSetPersister.NotFoundException {
        try {
            UserRequestDto userDto = userService.getUserById(id);
            UserResponseDto responseDto = new UserResponseDto(
                    id,
                    userDto.email(),
                    userDto.firstName(),
                    userDto.lastName()
            );
            return ResponseEntity.ok(responseDto);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) throws ChangeSetPersister.NotFoundException {
        try {
            UserRequestDto userDto = userService.getUserByEmail(email);
            UserResponseDto responseDto = new UserResponseDto(
                    userDto.userId(),
                    userDto.email(),
                    userDto.firstName(),
                    userDto.lastName()
            );
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
