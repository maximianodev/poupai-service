package com.maximianodev.poupai.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GenericResponseDTO {
  private final String timestamp;
  private final String message;

  public GenericResponseDTO(String message) {
    this.timestamp = LocalDateTime.now().toString();
    this.message = message;
  }
}
