package com.maximianodev.financial.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximianodev.financial.auth.dto.UserDTO;
import com.maximianodev.financial.auth.dto.UserLoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldReturnBadRequestForInvalidEmail() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setEmail("invalid-email");
    userDTO.setName("Test");
    userDTO.setPassword("Test@123");
    String user = objectMapper.writeValueAsString(userDTO);

    mockMvc
        .perform(post("/api/users/register").contentType("application/json").content(user))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForEmptyName() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setEmail("test@test.com");
    userDTO.setName("");
    userDTO.setPassword("Test@123");
    String user = objectMapper.writeValueAsString(userDTO);

    mockMvc
        .perform(post("/api/users/register").contentType("application/json").content(user))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForWeakPassword() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setEmail("test@test.com");
    userDTO.setName("Test");
    userDTO.setPassword("123");
    String user = objectMapper.writeValueAsString(userDTO);

    mockMvc
        .perform(post("/api/users/register").contentType("application/json").content(user))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForInvalidLoginEmail() throws Exception {
    UserLoginDTO userLoginDTO = new UserLoginDTO();
    userLoginDTO.setEmail("invalid-email");
    userLoginDTO.setPassword("Test@123");
    String userLogin = objectMapper.writeValueAsString(userLoginDTO);

    mockMvc
        .perform(post("/api/users/login").contentType("application/json").content(userLogin))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForEmptyPassword() throws Exception {
    UserLoginDTO userLoginDTO = new UserLoginDTO();
    userLoginDTO.setEmail("test@test.com");
    userLoginDTO.setPassword("");
    String userLogin = objectMapper.writeValueAsString(userLoginDTO);

    mockMvc
        .perform(post("/api/users/login").contentType("application/json").content(userLogin))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForEmptyEmail() throws Exception {
    UserLoginDTO userLoginDTO = new UserLoginDTO();
    userLoginDTO.setEmail("");
    userLoginDTO.setPassword("Test@123");
    String userLogin = objectMapper.writeValueAsString(userLoginDTO);

    mockMvc
        .perform(post("/api/users/login").contentType("application/json").content(userLogin))
        .andExpect(status().isBadRequest());
  }
}
