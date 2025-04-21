package com.maximianodev.poupai.exception;

public class InternalServerErrorException extends RuntimeException {
  public InternalServerErrorException(String message, Throwable cause) {
    super(message, cause);
  }
}