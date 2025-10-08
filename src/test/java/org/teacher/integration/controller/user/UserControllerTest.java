package org.teacher.integration.controller.user;

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
import org.teacher.dto.UserCredentialsDto;
import org.teacher.dto.request.UserRequestDto;
import org.teacher.entity.Role;
import org.springframework.http.HttpHeaders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String getAccessToken() throws Exception {
        var userCredentialsDto = new UserCredentialsDto("test@gmail.com","12345");

        String loginJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokens = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokens, JwtAuthenticationDto.class);

        return jwtAuthenticationDto.token();
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql"})
    void shouldCreateUser_whenUserDataIsValid() throws Exception {
        var userDto = new UserRequestDto(
                "test",
                "tes1",
                "te@gmail.com",
                "111222333",
                Set.of(Role.STUDENT)
        );

        String userJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnUserById_whenUserExists() throws Exception {
        String token = getAccessToken();
        mockMvc.perform(MockMvcRequestBuilders.get("/users/550e8400-e29b-41d4-a716-446655440000")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"));

    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnUserById_whenUserNotExists() throws Exception {
        String token = getAccessToken();
        mockMvc.perform(MockMvcRequestBuilders.get("/users/550e8400-e29b-41d4-a716-446655441111")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnUserById_whenUserIdIsWrong() throws Exception {
        String token = getAccessToken();
        mockMvc.perform(MockMvcRequestBuilders.get("/users/abc")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnUserByEmail_whenUserExists() throws Exception {

        String token = getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/users?email=test@gmail.com")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"));

    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnUserByEmail_whenUserNotExists() throws Exception {

        String token = getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/users?email=test_1@gmail.com")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());

    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnUserByEmail_whenEmptyEmail() throws Exception {
        String token = getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/users?email=")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void shouldReturnUserByEmail_whenFormatEmailIsWrong() throws Exception {
        String token = getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/users?email=test_1@gmail.comtest_1@gmail.com")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());

    }

}