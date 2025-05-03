package com.maximianodev.poupai.controller;

import static com.maximianodev.poupai.utils.Constants.Cookies.AUTH_COOKIE_NAME;
import static com.maximianodev.poupai.utils.Constants.SuccessMessages.SUCCESS_PASSWORD_RESET;
import static com.maximianodev.poupai.utils.Constants.SuccessMessages.SUCCESS_PASSWORD_RESET_LINK_SENT;
import static com.maximianodev.poupai.utils.Constants.SuccessMessages.SUCCESS_USER_LOGGED_IN;
import static com.maximianodev.poupai.utils.Constants.SuccessMessages.SUCCESS_USER_REGISTERED;

import com.maximianodev.poupai.dto.*;
import com.maximianodev.poupai.exception.BadRequestException;
import com.maximianodev.poupai.service.AuthService;
import com.maximianodev.poupai.utils.Constants;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<GenericResponseDTO> signUp(
      @RequestBody SignUpRequestDTO signUpRequestDTO, HttpServletResponse response)
      throws BadRequestException {
    final UserProfileDTO data = authService.signUp(signUpRequestDTO, response);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new UserResponseDataDTO(SUCCESS_USER_REGISTERED, data));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<GenericResponseDTO> signIn(
      @RequestBody AuthRequestDTO SignInRequestDTO, HttpServletResponse response)
      throws BadRequestException {
    final UserProfileDTO data = authService.signIn(SignInRequestDTO, response);
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
