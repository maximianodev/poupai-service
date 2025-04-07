package com.maximianodev.financial.auth.controller;

import static com.maximianodev.financial.auth.utils.Constants.Cookies.AUTH_COOKIE_NAME;
import static com.maximianodev.financial.auth.utils.Constants.SuccessMessages.*;
import static com.maximianodev.financial.auth.utils.Constants.SuccessMessages.SUCCESS_PASSWORD_RESET;

import com.maximianodev.financial.auth.dto.*;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.service.AuthService;
import io.jsonwebtoken.JwtException;
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
      @RequestBody RegisterRequestDTO registerRequestDTO) throws BadRequestException {
    ResponseCookie cookie = authService.registerUser(registerRequestDTO);

    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new GenericResponseDTO(SUCCESS_USER_REGISTERED));
  }

  @PostMapping("/login")
  public ResponseEntity<GenericResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO)
      throws BadRequestException {
    final UserDataDTO data = authService.loginUser(authRequestDTO);
    final String token = data.getToken().toString();
    final UserProfileDTO userProfile = new UserProfileDTO(data.getName(), data.getEmail());

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, token)
        .body(new UserResponseDataDTO(SUCCESS_USER_LOGGED_IN, userProfile));
  }

  @PostMapping("/logout")
  public ResponseEntity<GenericResponseDTO> logout() {
    ResponseCookie cookie = authService.logoutUser();

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .header(HttpHeaders.LOCATION, "/")
        .body(new GenericResponseDTO(SUCCESS_USER_LOGGED_OUT));
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
      @CookieValue(AUTH_COOKIE_NAME) String authToken, @RequestBody AuthRequestDTO requestBody)
      throws BadRequestException {
    ResponseCookie cookie = authService.resetPassword(authToken, requestBody);

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .header(HttpHeaders.LOCATION, "/")
        .body(new GenericResponseDTO(SUCCESS_PASSWORD_RESET));
  }

  @PostMapping("/validate-token")
  public ResponseEntity<GenericResponseDTO> validateToken(
      @CookieValue(AUTH_COOKIE_NAME) final String authToken) throws JwtException {
    authService.validateUserLoggedIn(authToken);

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, authToken)
        .body(new GenericResponseDTO(SUCCESS_USER_LOGGED_IN));
  }
}
