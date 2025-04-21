package com.maximianodev.poupai.utils;

import com.maximianodev.poupai.dto.SignUpRequestDTO;
import com.maximianodev.poupai.dto.AuthRequestDTO;
import com.maximianodev.poupai.exception.BadRequestException;

import static com.maximianodev.poupai.utils.Constants.ErrorMessages.*;

public class FieldsValidator {
  public static boolean isEmailValid(String email) {
    return !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  }

  public static boolean isPasswordValid(String password) {
    return !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
  }

  public static boolean isNameValid(String name) {
    return name.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");
  }

  public static void validateLoginFields(AuthRequestDTO user) throws BadRequestException {
    if (FieldsValidator.isEmailValid(user.getEmail())) {
      throw new BadRequestException(ERROR_INVALID_EMAIL);
    }
    if (FieldsValidator.isPasswordValid(user.getPassword())) {
      throw new BadRequestException(ERROR_INVALID_PASSWORD);
    }
  }

  public static void validateRegisterFields(SignUpRequestDTO user) throws BadRequestException {
    if (FieldsValidator.isEmailValid(user.getEmail())) {
      throw new BadRequestException(ERROR_INVALID_EMAIL);
    }

    if (FieldsValidator.isPasswordValid(user.getPassword())) {
      throw new BadRequestException(ERROR_INVALID_PASSWORD);
    }

    if (!FieldsValidator.isNameValid(user.getName())) {
      throw new BadRequestException(ERROR_INVALID_NAME);
    }
  }
}
