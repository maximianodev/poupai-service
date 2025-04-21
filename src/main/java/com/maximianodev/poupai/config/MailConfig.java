package com.maximianodev.poupai.config;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {
  @Value("${spring.smtp.email}")
  private String email;

  @Value("${spring.smtp.password}")
  private String password;

  @Bean
  public Session mailSession() {
    Properties props = new Properties();

    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    return Session.getInstance(
        props,
        new jakarta.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(email, password);
          }
        });
  }

  public void sendEmailMessage(String toEmail, String title, String content)
      throws MessagingException {
    Session session = mailSession();

    Message message = new MimeMessage(session);
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
    message.setSubject(title);
    message.setText(content);
    Transport.send(message);
  }
}
