package com.maximianodev.poupai.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximianodev.poupai.dto.AuthRequestDTO;
import com.maximianodev.poupai.dto.SignUpRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
  private final MockMvc mockMvc;
  private final ObjectMapper objectMapper;

  @Autowired
  public AuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
  }

  @Test
  void shouldReturnBadRequestForInvalidEmail() throws Exception {
    SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
    signUpRequestDTO.setEmail("invalid-email");
    signUpRequestDTO.setName("Test");
    signUpRequestDTO.setPassword("Test@123");
    String user = objectMapper.writeValueAsString(signUpRequestDTO);

    mockMvc
        .perform(post("/api/users/register").contentType("application/json").content(user))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForEmptyName() throws Exception {
    SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
    signUpRequestDTO.setEmail("test@test.com");
    signUpRequestDTO.setName("");
    signUpRequestDTO.setPassword("Test@123");
    String user = objectMapper.writeValueAsString(signUpRequestDTO);

    mockMvc
        .perform(post("/api/users/register").contentType("application/json").content(user))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForWeakPassword() throws Exception {
    SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
    signUpRequestDTO.setEmail("test@test.com");
    signUpRequestDTO.setName("Test");
    signUpRequestDTO.setPassword("123");
    String user = objectMapper.writeValueAsString(signUpRequestDTO);

    mockMvc
        .perform(post("/api/users/register").contentType("application/json").content(user))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForInvalidSignInEmail() throws Exception {
    AuthRequestDTO authRequestDTO = new AuthRequestDTO();
    authRequestDTO.setEmail("invalid-email");
    authRequestDTO.setPassword("Test@123");
    String userLogin = objectMapper.writeValueAsString(authRequestDTO);

    mockMvc
        .perform(post("/api/users/login").contentType("application/json").content(userLogin))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForEmptyPassword() throws Exception {
    AuthRequestDTO authRequestDTO = new AuthRequestDTO();
    authRequestDTO.setEmail("test@test.com");
    authRequestDTO.setPassword("");
    String userLogin = objectMapper.writeValueAsString(authRequestDTO);

    mockMvc
        .perform(post("/api/users/login").contentType("application/json").content(userLogin))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestForEmptyEmail() throws Exception {
    AuthRequestDTO authRequestDTO = new AuthRequestDTO();
    authRequestDTO.setEmail("");
    authRequestDTO.setPassword("Test@123");
    String userLogin = objectMapper.writeValueAsString(authRequestDTO);

    mockMvc
        .perform(post("/api/users/login").contentType("application/json").content(userLogin))
        .andExpect(status().isBadRequest());
  }
}
