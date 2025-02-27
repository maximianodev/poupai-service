package com.maximianodev.financial.auth.controller;

import com.maximianodev.financial.auth.dto.GenericResponse;
import com.maximianodev.financial.auth.dto.UserDTO;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.service.AlfredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired private AlfredService alfredService;

  @PostMapping("/register")
  public ResponseEntity<GenericResponse> register(@RequestBody UserDTO userDTO)
      throws BadRequestException {
    String jwtToken = alfredService.registerUser(userDTO);

    ResponseCookie cookie =
        ResponseCookie.from("Authorization", jwtToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofDays(1).getSeconds())
            .build();

    return ResponseEntity.status(HttpStatus.CREATED)
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new GenericResponse("User registered successfully"));
  }
}
