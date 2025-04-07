package com.maximianodev.financial.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseCookie;

@Getter
@Setter
@AllArgsConstructor
public class UserDataDTO {
  private String name;
  private String email;
  private ResponseCookie token;
}