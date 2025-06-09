package com.example.scheduletracker.controller;

import com.example.scheduletracker.config.jwt.JwtUtils;
import com.example.scheduletracker.dto.JwtResponse;
import com.example.scheduletracker.dto.LoginRequest;
import com.example.scheduletracker.dto.SignupRequest;
import com.example.scheduletracker.dto.SignupResponse;
import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.service.UserService;
import com.example.scheduletracker.service.NotificationService;
import com.example.scheduletracker.service.security.TotpService;
import com.example.scheduletracker.entity.VerificationToken;
import com.example.scheduletracker.repository.VerificationTokenRepository;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authManager;
  private final JwtUtils jwtUtils;
  private final UserService userService;
  private final TotpService totpService;
  private final NotificationService notificationService;
  private final VerificationTokenRepository tokenRepository;

  public AuthController(
      AuthenticationManager authManager,
      JwtUtils jwtUtils,
      UserService userService,
      TotpService totpService,
      NotificationService notificationService,
      VerificationTokenRepository tokenRepository) {
    this.authManager = authManager;
    this.jwtUtils = jwtUtils;
    this.userService = userService;
    this.totpService = totpService;
    this.notificationService = notificationService;
    this.tokenRepository = tokenRepository;
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest req) {
    Authentication auth =
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.username(), req.password()));
    String username = auth.getName();
    User user = userService.findByUsername(username).orElseThrow();
    if (!user.isActive()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    if (user.isTwoFaEnabled() && !totpService.verifyCode(user.getTwoFaSecret(), req.code())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    String token = jwtUtils.generateToken(username, user.getRole().name());
    return ResponseEntity.ok(new JwtResponse(token, username, user.getRole().name()));
  }

  @PostMapping("/register")
  public ResponseEntity<SignupResponse> register(@Valid @RequestBody SignupRequest req) {
    String username = req.username();
    Optional<User> existing = userService.findByUsername(username);
    if (existing.isPresent()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    String roleName = Optional.ofNullable(req.role()).orElse("STUDENT");
    String secret = totpService.generateSecret();
    User user =
        new User(
            null,
            username,
            req.password(),
            User.Role.valueOf(roleName.toUpperCase()),
            secret,
            false,
            false);
    userService.save(user);
    UUID token = java.util.UUID.randomUUID();
    tokenRepository.save(new VerificationToken(token, user));
    notificationService.sendEmail(
        username, "Verify account", "/api/auth/verify?token=" + token.toString());
    return ResponseEntity.status(HttpStatus.CREATED).body(new SignupResponse(secret));
  }

  @GetMapping("/verify")
  public ResponseEntity<Void> verify(@RequestParam UUID token) {
    return tokenRepository
        .findByToken(token)
        .map(
            vt -> {
              User user = vt.getUser();
              user.setActive(true);
              userService.update(user);
              tokenRepository.delete(vt);
              return ResponseEntity.ok().<Void>build();
            })
        .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
  }
}
