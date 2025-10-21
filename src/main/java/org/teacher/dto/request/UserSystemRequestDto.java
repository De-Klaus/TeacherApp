package org.teacher.dto.request;

import org.teacher.entity.Role;

import java.util.Set;

public record UserSystemRequestDto(
        String firstName,
        String lastName,
        Set<Role> roles
) {
}
