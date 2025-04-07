package com.maximianodev.financial.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileDTO {
  private String name;
  private String email;
}
