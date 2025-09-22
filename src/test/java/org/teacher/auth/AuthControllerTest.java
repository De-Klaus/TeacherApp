package org.teacher.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.teacher.dto.JwtAuthenticationDto;
import org.teacher.dto.UserCredentialsDto;
import org.teacher.security.jwt.JwtService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sing-in")
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

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sing-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshTest() {

    }
}