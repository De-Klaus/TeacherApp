package org.teacher.service;

import org.teacher.dto.response.UserResponseDto;

public interface AuthService {
    UserResponseDto getCurrentUser();
}
