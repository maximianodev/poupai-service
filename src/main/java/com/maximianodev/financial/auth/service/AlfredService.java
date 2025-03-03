package com.maximianodev.financial.auth.service;

import com.maximianodev.financial.auth.dto.UserLoginDTO;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.repository.UserRepository;
import com.maximianodev.financial.auth.dto.UserDTO;
import com.maximianodev.financial.auth.model.User;
import com.maximianodev.financial.auth.utils.FieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.maximianodev.financial.auth.utils.Constants.*;

@Service
public class AlfredService {
  @Autowired private UserRepository userRepository;
  @Autowired private JwtService jwtService;

  public ResponseCookie registerUser(UserDTO userDTO) throws BadRequestException {
    validateFields(userDTO);

    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    User user = new User();
    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
    userRepository.save(user);

    return createResponseCookie(jwtService.generateToken(user.getEmail()));
  }

  public ResponseCookie loginUser(UserLoginDTO userLoginDTO) throws BadRequestException {
    validateFields(userLoginDTO);

    User user = userRepository.findByEmail(userLoginDTO.getEmail());
    if (user == null
        || !new BCryptPasswordEncoder().matches(userLoginDTO.getPassword(), user.getPassword())) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    return createResponseCookie(jwtService.generateToken(user.getEmail()));
  }

  public ResponseCookie logoutUser() {
    return createResponseCookie("");
  }

  private void validateFields(UserLoginDTO user) throws BadRequestException {
    if (!FieldsValidator.isEmailValid(user.getEmail())) {
      throw new BadRequestException(ERROR_INVALID_EMAIL);
    }
    if (!FieldsValidator.isPasswordValid(user.getPassword())) {
      throw new BadRequestException(ERROR_INVALID_PASSWORD);
    }
  }

  private void validateFields(UserDTO user) throws BadRequestException {
    if (!FieldsValidator.isEmailValid(user.getEmail())) {
      throw new BadRequestException(ERROR_INVALID_EMAIL);
    }
    if (!FieldsValidator.isPasswordValid(user.getPassword())) {
      throw new BadRequestException(ERROR_INVALID_PASSWORD);
    }
    if (!FieldsValidator.isNameValid(user.getName())) {
      throw new BadRequestException(ERROR_INVALID_NAME);
    }
  }

  private ResponseCookie createResponseCookie(String token) {
    return ResponseCookie.from("Authorization", token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(Duration.ofDays(1).getSeconds())
        .build();
  }
}
