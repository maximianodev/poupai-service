package com.maximianodev.financial.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
  @Value("${spring.jwt.secret}")
  private String secretKey;

  public String generateToken(String subject) {
    return Jwts.builder()
        .subject(subject)
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .compact();
  }
}
