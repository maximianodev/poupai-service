package com.maximianodev.financial.auth.controller;

import static com.maximianodev.financial.auth.utils.Constants.Cookies.AUTH_COOKIE_NAME;
import static com.maximianodev.financial.auth.utils.Constants.SuccessMessages.*;
import static com.maximianodev.financial.auth.utils.Constants.SuccessMessages.SUCCESS_PASSWORD_RESET;

import com.maximianodev.financial.auth.dto.*;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final AuthService authService;

  public UserController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<GenericResponseDTO> register(@RequestBody UserDTO userDTO)
      throws BadRequestException {
    ResponseCookie cookie = authService.registerUser(userDTO);

    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new GenericResponseDTO(SUCCESS_USER_REGISTERED));
  }

  @PostMapping("/login")
  public ResponseEntity<GenericResponseDTO> login(@RequestBody UserLoginDTO userLoginDTO)
      throws BadRequestException {
    ResponseCookie cookie = authService.loginUser(userLoginDTO);

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new GenericResponseDTO(SUCCESS_USER_LOGGED_IN));
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
  public ResponseEntity<GenericResponseDTO> forgotPassword(@RequestBody EmailDTO request)
      throws BadRequestException {
    authService.forgotPassword(request);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new GenericResponseDTO(SUCCESS_PASSWORD_RESET_LINK_SENT));
  }

  @PutMapping("/reset-password")
  public ResponseEntity<GenericResponseDTO> resetPassword(
      @RequestHeader(AUTH_COOKIE_NAME) String authToken, @RequestBody ResetPasswordDTO requestBody)
      throws BadRequestException {
    ResponseCookie cookie = authService.resetPassword(authToken, requestBody);

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .header(HttpHeaders.LOCATION, "/")
        .body(new GenericResponseDTO(SUCCESS_PASSWORD_RESET));
  }
}
