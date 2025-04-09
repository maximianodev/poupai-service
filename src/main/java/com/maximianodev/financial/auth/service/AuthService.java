package com.maximianodev.financial.auth.service;

import static com.maximianodev.financial.auth.utils.Constants.Cookies.*;
import static com.maximianodev.financial.auth.utils.Constants.ErrorMessages.*;
import static com.maximianodev.financial.auth.utils.FieldsValidator.validateLoginFields;
import static com.maximianodev.financial.auth.utils.FieldsValidator.validateRegisterFields;

import com.maximianodev.financial.auth.dto.AuthRequestDTO;
import com.maximianodev.financial.auth.dto.ForgotPasswordRequestDTO;
import com.maximianodev.financial.auth.dto.RegisterRequestDTO;
import com.maximianodev.financial.auth.dto.UserProfileDTO;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.model.User;
import com.maximianodev.financial.auth.repository.UserRepository;
import com.maximianodev.financial.auth.utils.FieldsValidator;
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

  public UserProfileDTO registerUser(
      final RegisterRequestDTO registerRequestDTO, HttpServletResponse response)
      throws BadRequestException {
    validateRegisterFields(registerRequestDTO);

    if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
      throw new BadRequestException(ERROR_BAD_REQUEST);
    }

    final String password = new BCryptPasswordEncoder().encode(registerRequestDTO.getPassword());

    User user = new User();
    user.setName(registerRequestDTO.getName());
    user.setEmail(registerRequestDTO.getEmail());
    user.setPassword(password);

    userRepository.save(user);
    createResponseCookie(jwtService.generateToken(user.getEmail()), response);

    return new UserProfileDTO(user.getName(), user.getEmail());
  }

  public UserProfileDTO loginUser(final AuthRequestDTO authRequestDTO, HttpServletResponse response)
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
