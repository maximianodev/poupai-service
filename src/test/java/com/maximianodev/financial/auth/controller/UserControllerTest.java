package com.maximianodev.financial.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximianodev.financial.auth.dto.UserDTO;
import com.maximianodev.financial.auth.repository.UserRepository;
import com.maximianodev.financial.auth.service.AlfredService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private AlfredService alfredService;
  @Autowired private UserRepository userRepository;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldBadRequestForExistingUser() throws Exception {
    UserDTO userDTO = new UserDTO();

    userDTO.setEmail("test@test.com");
    userDTO.setName("Test");
    userDTO.setPassword("Test@123");
    String user = objectMapper.writeValueAsString(userDTO);

    alfredService.registerUser(userDTO);

    mockMvc
        .perform(post("/api/users/register").contentType("application/json").content(user))
        .andExpect(status().isBadRequest());

    userRepository.delete(userRepository.findByEmail(userDTO.getEmail()));
  }

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
}
