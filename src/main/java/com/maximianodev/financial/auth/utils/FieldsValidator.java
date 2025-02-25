package com.maximianodev.financial.auth.utils;

/** Utility class for validating various fields such as email, password, and name. */
public class FieldsValidator {
  /**
   * Validates if the provided email is in a valid format.
   *
   * @param email the email to validate
   * @return true if the email is valid, false otherwise
   */
  public static boolean isEmailValid(String email) {
    return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  }

  /**
   * Validates if the provided password meets the required criteria. The password must contain at
   * least one digit, one lowercase letter, one uppercase letter, one special character, and be at
   * least 8 characters long.
   *
   * @param password the password to validate
   * @return true if the password is valid, false otherwise
   */
  public static boolean isPasswordValid(String password) {
    return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
  }

  /**
   * Validates if the provided name is in a valid format. The name can contain letters, spaces, and
   * certain punctuation characters.
   *
   * @param name the name to validate
   * @return true if the name is valid, false otherwise
   */
  public static boolean isNameValid(String name) {
    return name.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");
  }
}
