// src/main/java/com/example/scheduletracker/controller/UserController.java
package com.example.scheduletracker.controller;

import com.example.scheduletracker.dto.SignupResponse;
import com.example.scheduletracker.dto.UserDto;
import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.mapper.UserMapper;
import com.example.scheduletracker.service.UserService;
import com.example.scheduletracker.service.security.TotpService;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService svc;
  private final UserMapper mapper;
  private final TotpService totpService;

  public UserController(UserService svc, UserMapper mapper, TotpService totpService) {
    this.svc = svc;
    this.mapper = mapper;
    this.totpService = totpService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> currentUser(
      org.springframework.security.core.Authentication auth) {
    String username = auth.getName();
    return svc.findByUsername(username)
        .map(mapper::toDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{username}")
  public ResponseEntity<UserDto> getByUsername(@PathVariable String username) {
    Optional<User> user = svc.findByUsername(username);
    return user.map(mapper::toDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public UserDto create(@RequestBody UserDto user) {
    User saved = svc.save(mapper.toEntity(user));
    return mapper.toDto(saved);
  }

  @PostMapping("/me/2fa/enable")
  public SignupResponse enable2fa(org.springframework.security.core.Authentication auth) {
    String username = auth.getName();
    User user = svc.findByUsername(username).orElseThrow();
    if (!user.isTwoFaEnabled()) {
      String secret = totpService.generateSecret();
      user.setTwoFaSecret(secret);
      user.setTwoFaEnabled(true);
    }
    svc.update(user);
    return new SignupResponse(user.getTwoFaSecret());
  }

  @PostMapping("/me/2fa/disable")
  public void disable2fa(org.springframework.security.core.Authentication auth) {
    String username = auth.getName();
    User user = svc.findByUsername(username).orElseThrow();
    user.setTwoFaEnabled(false);
    svc.update(user);
  }
}
