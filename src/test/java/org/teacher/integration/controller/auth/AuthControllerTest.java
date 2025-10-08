package org.teacher.integration.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.dto.RefreshTokenDto;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.security.jwt.JwtService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    // Style «should…when…»
    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnJwtToken_whenCredentialsAreValid() throws Exception {
        var userCredentialsDto = new UserCredentialsDto("test@gmail.com","12345");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var jwtAuthenticationDto = objectMapper.readValue(tokenJson, JwtAuthenticationDto.class);

        assertEquals(userCredentialsDto.email(), jwtService.getEmailFromToken(jwtAuthenticationDto.token()));
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldThrowAuthenticationException_whenCredentialsAreInvalid() throws Exception {
        var userCredentialsDto = new UserCredentialsDto("test@gmail.com","125555");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnRefreshJwtToken_whenCredentialsAreValid() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("test@gmail.com", "12345");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(objectMapper
                .readValue(tokenJson, JwtAuthenticationDto.class)
                .refreshToken());

        String refreshTokenJson = objectMapper.writeValueAsString(refreshTokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshTokenJson))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldThrowAuthenticationRefreshException_whenCredentialsAreInvalid() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto("test@gmail.com", "12345");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);
        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtDto = objectMapper.readValue(tokenJson, JwtAuthenticationDto.class);

        // Подменяем refresh token
        RefreshTokenDto invalidRefreshTokenDto = new RefreshTokenDto(jwtDto.refreshToken() + "tampered");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRefreshTokenDto)))
                .andExpect(status().isUnauthorized());
    }
}