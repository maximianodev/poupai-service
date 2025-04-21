package com.maximianodev.poupai.service;

import static com.maximianodev.poupai.utils.Constants.Cookies.*;
import static com.maximianodev.poupai.utils.Constants.ErrorMessages.*;
import static com.maximianodev.poupai.utils.FieldsValidator.validateLoginFields;
import static com.maximianodev.poupai.utils.FieldsValidator.validateRegisterFields;

import com.maximianodev.poupai.dto.AuthRequestDTO;
import com.maximianodev.poupai.dto.ForgotPasswordRequestDTO;
import com.maximianodev.poupai.dto.SignUpRequestDTO;
import com.maximianodev.poupai.dto.UserProfileDTO;
import com.maximianodev.poupai.exception.BadRequestException;
import com.maximianodev.poupai.model.User;
import com.maximianodev.poupai.repository.UserRepository;
import com.maximianodev.poupai.utils.FieldsValidator;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

  public UserProfileDTO signUp(
          final SignUpRequestDTO signUpRequestDTO, HttpServletResponse response)
      throws BadRequestException {
    validateRegisterFields(signUpRequestDTO);

    if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    final String password = new BCryptPasswordEncoder().encode(signUpRequestDTO.getPassword());

    User user = new User();
    user.setName(signUpRequestDTO.getName());
    user.setEmail(signUpRequestDTO.getEmail());
    user.setPassword(password);

    userRepository.save(user);
    createResponseCookie(jwtService.generateToken(user.getEmail()), response);

    return new UserProfileDTO(user.getName(), user.getEmail());
  }

  public UserProfileDTO signIn(final AuthRequestDTO authRequestDTO, HttpServletResponse response)
      throws BadRequestException {
    validateLoginFields(authRequestDTO);

    final User user = userRepository.findByEmail(authRequestDTO.getEmail());

    if (user == null) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    final boolean isValidPassword =
        new BCryptPasswordEncoder().matches(authRequestDTO.getPassword(), user.getPassword());

    if (!isValidPassword) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    createResponseCookie(jwtService.generateToken(user.getEmail()), response);
    return new UserProfileDTO(user.getName(), user.getEmail());
  }

  public void forgotPassword(final ForgotPasswordRequestDTO request) throws BadRequestException {
    final String email = request.getEmail();

    if (FieldsValidator.isEmailValid(email)) {
      throw new BadRequestException(ERROR_INVALID_EMAIL);
    }

    final User user = userRepository.findByEmail(email);

    if (user == null) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    emailService.recoverPassword(user.getEmail());
  }

  public UserProfileDTO resetPassword(
      final String token, final AuthRequestDTO requestBody, HttpServletResponse response)
      throws BadRequestException {
    final String email = jwtService.getSubject(token);

    if (email == null) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    final User user = userRepository.findByEmail(email);

    if (user == null) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    if (FieldsValidator.isPasswordValid(requestBody.getPassword())) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    final String password = new BCryptPasswordEncoder().encode(requestBody.getPassword());
    user.setPassword(password);

    userRepository.save(user);

    createResponseCookie(jwtService.generateToken(user.getEmail()), response);
    return new UserProfileDTO(user.getName(), user.getEmail());
  }

  public void validateUserLoggedIn(final String token) throws JwtException {
    if (token == null || token.isEmpty()) {
      throw new JwtException(ERROR_INVALID_TOKEN);
    }

    final String email = jwtService.getSubject(token);

    if (email == null) {
      throw new JwtException(ERROR_INVALID_TOKEN);
    }
  }

  private static void createResponseCookie(final String token, HttpServletResponse response) {
    Cookie jwtCookie = new Cookie(AUTH_COOKIE_NAME, token);
    jwtCookie.setHttpOnly(AUTH_COOKIE_HTTP_ONLY);
    jwtCookie.setSecure(AUTH_COOKIE_SECURE);
    jwtCookie.setMaxAge((int) AUTH_COOKIE_MAX_AGE);
    jwtCookie.setPath(AUTH_COOKIE_PATH);
    response.addCookie(jwtCookie);
  }
}
