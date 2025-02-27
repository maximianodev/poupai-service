package com.maximianodev.financial.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
  private final SecretKey secretKey;

  public JwtService(@Value("${jwt.secret}") String secretKey) {
    this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public String generateToken(String subject) {
    return Jwts.builder()
        .subject(subject)
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
        .signWith(secretKey)
        .compact();
  }

  public boolean validateUser(String token, String email) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject()
        .equals(email);
  }
}
