package com.maximianodev.financial.auth.service;

import static com.maximianodev.financial.auth.utils.Constants.Email.*;
import static com.maximianodev.financial.auth.utils.Constants.ErrorMessages.ERROR_SENDING_EMAIL;

import com.maximianodev.financial.auth.config.MailConfig;
import com.maximianodev.financial.auth.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  @Value("${spring.application.url}")
  private String applicationUrl;

  @Autowired private JwtService jwtService;
  @Autowired private MailConfig mailConfig;

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
