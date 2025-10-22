package org.teacher.controller.registr;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.service.UserService;

import javax.naming.AuthenticationException;
import java.util.UUID;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<JwtAuthenticationDto> registerByToken(@RequestParam("token") UUID claimToken) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = userService.registerStudent(claimToken);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
