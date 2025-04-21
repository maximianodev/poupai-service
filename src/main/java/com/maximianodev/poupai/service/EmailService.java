package com.maximianodev.poupai.service;

import static com.maximianodev.poupai.utils.Constants.Email.*;
import static com.maximianodev.poupai.utils.Constants.ErrorMessages.ERROR_SENDING_EMAIL;

import com.maximianodev.poupai.config.MailConfig;
import com.maximianodev.poupai.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  @Value("${spring.application.url}")
  private String applicationUrl;

  private final JwtService jwtService;
  private final MailConfig mailConfig;

  public EmailService(JwtService jwtService, MailConfig mailConfig) {
    this.jwtService = jwtService;
    this.mailConfig = mailConfig;
  }

  public void recoverPassword(String email) throws InternalServerErrorException {
    String jwtToken = jwtService.generateToken(email);

    try {
      mailConfig.sendEmailMessage(
          email,
          RECOVERY_MESSAGE_TITLE,
          String.format(RECOVERY_MESSAGE_CONTENT, applicationUrl, jwtToken));
    } catch (Exception e) {
      throw new InternalServerErrorException(ERROR_SENDING_EMAIL, e);
    }
  }
}
