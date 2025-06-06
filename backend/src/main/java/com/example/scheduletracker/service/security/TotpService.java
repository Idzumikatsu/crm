package com.example.scheduletracker.service.security;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.stereotype.Service;

@Service
public class TotpService {
  public String generateSecret() {
    return Base32.random();
  }

  public boolean verifyCode(String secret, String code) {
    if (secret == null || code == null) {
      return false;
    }
    Totp totp = new Totp(secret);
    return totp.verify(code);
  }
}
