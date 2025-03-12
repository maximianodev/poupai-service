package com.maximianodev.financial.auth.controller;

import static com.maximianodev.financial.auth.utils.Constants.SuccessMessages.*;

import com.maximianodev.financial.auth.dto.EmailDTO;
import com.maximianodev.financial.auth.dto.GenericResponse;
import com.maximianodev.financial.auth.dto.UserDTO;
import com.maximianodev.financial.auth.dto.UserLoginDTO;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final AuthService authService;

  public UserController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<GenericResponse> register(@RequestBody UserDTO userDTO)
      throws BadRequestException {
    ResponseCookie cookie = authService.registerUser(userDTO);

    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new GenericResponse(SUCCESS_USER_REGISTERED));
  }

  @PostMapping("/login")
  public ResponseEntity<GenericResponse> login(@RequestBody UserLoginDTO userLoginDTO)
      throws BadRequestException {
    ResponseCookie cookie = authService.loginUser(userLoginDTO);

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new GenericResponse(SUCCESS_USER_LOGGED_IN));
  }

  @PostMapping("/logout")
  public ResponseEntity<GenericResponse> logout() {
    ResponseCookie cookie = authService.logoutUser();

    return ResponseEntity.status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .header(HttpHeaders.LOCATION, "/")
        .body(new GenericResponse(SUCCESS_USER_LOGGED_OUT));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<GenericResponse> forgotPassword(@RequestBody EmailDTO request)
      throws BadRequestException {
    authService.forgotPassword(request);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new GenericResponse(SUCCESS_PASSWORD_RESET_LINK_SENT));
  }
}
