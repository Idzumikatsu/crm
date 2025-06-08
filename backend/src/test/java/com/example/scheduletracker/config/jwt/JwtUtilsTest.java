package com.example.scheduletracker.config.jwt;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilsTest {
  private JwtUtils utils;

  @BeforeEach
  void setup() {
    utils = new JwtUtils("0123456789abcdef0123456789abcdef");
  }

  @Test
  void generateAndParseToken() {
    String token = utils.generateToken("alice", "MANAGER");
    Claims claims = utils.parse(token);
    assertEquals("alice", claims.getSubject());
    assertEquals("MANAGER", claims.get("role"));
  }
}
