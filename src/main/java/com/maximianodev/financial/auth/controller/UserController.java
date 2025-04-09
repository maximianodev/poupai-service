package com.maximianodev.financial.auth.controller;

import static com.maximianodev.financial.auth.utils.Constants.Cookies.AUTH_COOKIE_NAME;
import static com.maximianodev.financial.auth.utils.Constants.SuccessMessages.*;
import static com.maximianodev.financial.auth.utils.Constants.SuccessMessages.SUCCESS_PASSWORD_RESET;

import com.maximianodev.financial.auth.dto.*;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.service.AuthService;
import com.maximianodev.financial.auth.utils.Constants;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final AuthService authService;

  public UserController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<GenericResponseDTO> register(
      @RequestBody RegisterRequestDTO registerRequestDTO, HttpServletResponse response)
      throws BadRequestException {
    final UserProfileDTO data = authService.registerUser(registerRequestDTO, response);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new UserResponseDataDTO(SUCCESS_USER_REGISTERED, data));
  }

  @PostMapping("/login")
  public ResponseEntity<GenericResponseDTO> login(
      @RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response)
      throws BadRequestException {
    final UserProfileDTO data = authService.loginUser(authRequestDTO, response);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new UserResponseDataDTO(SUCCESS_USER_LOGGED_IN, data));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<GenericResponseDTO> forgotPassword(
      @RequestBody ForgotPasswordRequestDTO request) throws BadRequestException {
    authService.forgotPassword(request);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new GenericResponseDTO(SUCCESS_PASSWORD_RESET_LINK_SENT));
  }

  @PutMapping("/reset-password")
  public ResponseEntity<GenericResponseDTO> resetPassword(
      @CookieValue(value = AUTH_COOKIE_NAME) String authToken,
      @RequestBody AuthRequestDTO requestBody,
      HttpServletResponse response)
      throws BadRequestException {
    final UserProfileDTO data = authService.resetPassword(authToken, requestBody, response);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new UserResponseDataDTO(SUCCESS_PASSWORD_RESET, data));
  }

  @PostMapping("/validate-token")
  public ResponseEntity<GenericResponseDTO> validateToken(HttpServletRequest request)
      throws JwtException {
    Cookie[] cookies = request.getCookies();
    String Authorization = Constants.Cookies.getCookieValue(cookies, AUTH_COOKIE_NAME);

    authService.validateUserLoggedIn(Authorization);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new GenericResponseDTO(SUCCESS_USER_LOGGED_IN));
  }
}
