package org.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.teacher.model.Role;
import org.teacher.model.Tariff;
import org.teacher.model.User;
import org.teacher.repository.TariffRepository;
import org.teacher.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository tariffRepository;
    private final UserRepository userRepository;

    public List<Tariff> getTariffsForTeacher(String username) {
        User teacher = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!teacher.getRoles().contains(Role.TEACHER)) {
            throw new AccessDeniedException("User is not a teacher");
        }

        return tariffRepository.findByTeacherId(teacher);
    }
}

