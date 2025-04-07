package com.maximianodev.financial.auth.dto;

import lombok.Getter;

@Getter
public class UserResponseDataDTO extends GenericResponseDTO {
  private final UserProfileDTO data;

  public UserResponseDataDTO(String message, UserProfileDTO data) {
    super(message);
    this.data = data;
  }
}
