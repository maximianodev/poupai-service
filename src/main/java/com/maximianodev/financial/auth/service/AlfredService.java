package com.maximianodev.financial.auth.service;

import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.repository.UserRepository;
import com.maximianodev.financial.auth.dto.UserDTO;
import com.maximianodev.financial.auth.model.User;
import com.maximianodev.financial.auth.utils.FieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.maximianodev.financial.auth.utils.Constants.ERROR_BAD_REQUEST;
import static com.maximianodev.financial.auth.utils.Constants.ERROR_INVALID_EMAIL;
import static com.maximianodev.financial.auth.utils.Constants.ERROR_INVALID_NAME;
import static com.maximianodev.financial.auth.utils.Constants.ERROR_INVALID_PASSWORD;

@Service
public class AlfredService {
  @Autowired private UserRepository userRepository;
  @Autowired private JwtService jwtService;

  public String registerUser(UserDTO userDTO) throws BadRequestException {
    this.fieldsValidator(userDTO);

    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    User user = new User();
    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());

    String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());
    user.setPassword(encryptedPassword);

    userRepository.save(user);

    return jwtService.generateToken(user.getEmail());
  }

  private void fieldsValidator(UserDTO user) throws BadRequestException {
    boolean isValidEmail = FieldsValidator.isEmailValid(user.getEmail());
    boolean isValidPassword = FieldsValidator.isPasswordValid(user.getPassword());
    boolean isValidName = FieldsValidator.isNameValid(user.getName());

    if (!isValidEmail) {
      throw new BadRequestException(ERROR_INVALID_EMAIL);
    }

    if (!isValidPassword) {
      throw new BadRequestException(ERROR_INVALID_PASSWORD);
    }

    if (!isValidName) {
      throw new BadRequestException(ERROR_INVALID_NAME);
    }
  }
}
