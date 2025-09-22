package org.teacher.dto;

public record UserDto(
        String userId,
        String firstName,
        String lastName,
        String email,
        String password) {
}
