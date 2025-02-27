package com.maximianodev.financial.auth.controller;

import com.maximianodev.financial.auth.dto.GenericResponse;
import com.maximianodev.financial.auth.exception.BadRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler class is responsible for handling exceptions thrown by the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<GenericResponse> handleBadRequestException(BadRequestException exception) {
    GenericResponse errorResponse = new GenericResponse(exception.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<GenericResponse> handleExpiredJwtException(ExpiredJwtException exception) {
    GenericResponse errorResponse = new GenericResponse(exception.getMessage());

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<GenericResponse> handleJwtException(JwtException exception) {
    GenericResponse errorResponse = new GenericResponse(exception.getMessage());

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }
}
