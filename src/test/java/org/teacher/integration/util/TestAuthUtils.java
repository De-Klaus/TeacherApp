package org.teacher.integration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.dto.UserCredentialsDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAuthUtils {

    private TestAuthUtils() {}

    /**
     * Gets a JWT token for a test user.
     */
    public static String obtainAccessToken(MockMvc mockMvc, ObjectMapper objectMapper, String email, String password) throws Exception {
        String loginJson = objectMapper.writeValueAsString(new UserCredentialsDto(email,password));

        String tokens = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sing-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokens, JwtAuthenticationDto.class);
        return jwtAuthenticationDto.token();
    }

    /**
     * Возвращает заголовок Authorization с токеном.
     */
    public static String authHeader(String token) {
        return "Bearer " + token;
    }
}
