package com.lmaximiano.financial.auth.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GenericResponse {
  private final LocalDateTime timestamp;
  private final String message;

  public GenericResponse(String message) {
    this.timestamp = LocalDateTime.now();
    this.message = message;
  }
}
