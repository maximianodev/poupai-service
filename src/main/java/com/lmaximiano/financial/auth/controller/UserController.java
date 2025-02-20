package com.lmaximiano.financial.auth.controller;

import com.lmaximiano.financial.auth.dto.GenericResponse;
import com.lmaximiano.financial.auth.dto.UserDTO;
import com.lmaximiano.financial.auth.exception.BadRequestException;
import com.lmaximiano.financial.auth.service.AlfredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired private AlfredService alfredService;

  @PostMapping("/register")
  public ResponseEntity<GenericResponse> register(@RequestBody UserDTO userDTO)
      throws BadRequestException {
    alfredService.registerUser(userDTO);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new GenericResponse("User registered successfully"));
  }
}
