package com.maximianodev.financial.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
  private String email;
  private String password;
}
