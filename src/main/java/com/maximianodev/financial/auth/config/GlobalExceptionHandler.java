package com.maximianodev.financial.auth.config;

import com.maximianodev.financial.auth.dto.GenericResponseDTO;
import com.maximianodev.financial.auth.exception.BadRequestException;
import com.maximianodev.financial.auth.exception.InternalServerErrorException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.maximianodev.financial.auth.utils.Constants.ErrorMessages.*;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<GenericResponseDTO> handleBadRequestException(
      BadRequestException exception) {
    GenericResponseDTO errorResponse = new GenericResponseDTO(exception.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<GenericResponseDTO> handleExpiredJwtException(
      ExpiredJwtException exception) {
    GenericResponseDTO errorResponse = new GenericResponseDTO(ERROR_EXPIRED_TOKEN);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<GenericResponseDTO> handleJwtException(JwtException exception) {
    GenericResponseDTO errorResponse = new GenericResponseDTO(ERROR_INVALID_TOKEN);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(MissingRequestCookieException.class)
  public ResponseEntity<GenericResponseDTO> handleJwtException(
      MissingRequestCookieException exception) {
    GenericResponseDTO errorResponse = new GenericResponseDTO(ERROR_INVALID_COOKIE_TOKEN);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<GenericResponseDTO> handleInternalServerErrorException(
      InternalServerErrorException exception) {
    GenericResponseDTO errorResponse = new GenericResponseDTO(exception.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GenericResponseDTO> handleGenericException(Exception exception) {
    GenericResponseDTO errorResponse = new GenericResponseDTO(ERROR_GENERIC);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
