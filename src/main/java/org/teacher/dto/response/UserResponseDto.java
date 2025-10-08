package org.teacher.dto.response;

import org.teacher.entity.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        Set<Role> roles
) {
    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }
}
