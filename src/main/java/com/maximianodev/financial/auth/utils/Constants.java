package com.maximianodev.financial.auth.utils;

public class Constants {
  public static class ErrorMessages {
    public static final String ERROR_GENERIC = "Internal Server Error";
    public static final String ERROR_BAD_REQUEST = "Bad Request";
    public static final String ERROR_INVALID_EMAIL = "Invalid Email";
    public static final String ERROR_INVALID_PASSWORD = "Invalid Password";
    public static final String ERROR_INVALID_NAME = "Invalid Name";
    public static final String ERROR_SENDING_EMAIL = "Error sending email";
  }

  public static class SuccessMessages {
    public static final String SUCCESS_USER_REGISTERED = "User registered successfully";
    public static final String SUCCESS_USER_LOGGED_IN = "User logged in successfully";
    public static final String SUCCESS_USER_LOGGED_OUT = "User logged out successfully";
    public static final String SUCCESS_PASSWORD_RESET_LINK_SENT =
        "Password reset link sent to your email";
  }

  public static class Email {
    public static final String RECOVERY_MESSAGE_TITLE = "Clique aqui %s/reset-password?token=%s";
    public static final String RECOVERY_MESSAGE_CONTENT = "Clique aqui %s/reset-password?token=%s";
  }
}
