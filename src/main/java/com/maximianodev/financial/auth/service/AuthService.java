package com.maximianodev.financial.auth.service;

import static com.maximianodev.financial.auth.utils.Constants.ErrorMessages.ERROR_BAD_REQUEST;
import static com.maximianodev.financial.auth.utils.Constants.ErrorMessages.ERROR_INVALID_EMAIL;
import static com.maximianodev.financial.auth.utils.FieldsValidator.validateLoginFields;
import static com.maximianodev.financial.auth.utils.FieldsValidator.validateRegisterFields;

import com.maximianodev.financial.auth.dto.EmailDTO;
import com.maximianodev.financial.auth.dto.UserDTO;
import com.maximianodev.financial.auth.dto.UserLoginDTO;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.model.User;
import com.maximianodev.financial.auth.repository.UserRepository;
import com.maximianodev.financial.auth.utils.FieldsValidator;
import java.time.Duration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final EmailService emailService;

  public AuthService(
      UserRepository userRepository, JwtService jwtService, EmailService emailService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.emailService = emailService;
  }

  public ResponseCookie registerUser(UserDTO userDTO) throws BadRequestException {
    validateRegisterFields(userDTO);

    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    String password = new BCryptPasswordEncoder().encode(userDTO.getPassword());

    User user = new User();
    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(password);

    userRepository.save(user);

    return createResponseCookie(jwtService.generateToken(user.getEmail()));
  }

  public ResponseCookie loginUser(UserLoginDTO userLoginDTO) throws BadRequestException {
    validateLoginFields(userLoginDTO);

    User user = userRepository.findByEmail(userLoginDTO.getEmail());

    if (user == null) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    boolean isPasswordValid =
        new BCryptPasswordEncoder().matches(userLoginDTO.getPassword(), user.getPassword());

    if (!isPasswordValid) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    String jwtToken = jwtService.generateToken(user.getEmail());

    return createResponseCookie(jwtToken);
  }

  public ResponseCookie logoutUser() {
    return createResponseCookie("");
  }

  public void forgotPassword(EmailDTO request) throws BadRequestException {
    String email = request.getEmail();

    if (FieldsValidator.isEmailValid(email)) {
      throw new BadRequestException(ERROR_INVALID_EMAIL);
    }

    User user = userRepository.findByEmail(email);

    if (user == null) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    emailService.recoverPassword(user.getEmail());
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
