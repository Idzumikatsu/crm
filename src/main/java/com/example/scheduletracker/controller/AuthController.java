package com.example.scheduletracker.controller;

import com.example.scheduletracker.config.jwt.JwtUtils;
import com.example.scheduletracker.dto.JwtResponse;
import com.example.scheduletracker.dto.LoginRequest;
import com.example.scheduletracker.dto.SignupRequest;
import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authManager;
  private final JwtUtils jwtUtils;
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {
    Authentication auth =
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
    String username = auth.getName();
    User user = userService.findByUsername(username).orElseThrow();
    String token = jwtUtils.generateToken(username, user.getRole().name());
    return ResponseEntity.ok(new JwtResponse(token, username, user.getRole().name()));
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody SignupRequest req) {
    String username = req.getUsername();
    Optional<User> existing = userService.findByUsername(username);
    if (existing.isPresent()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    String roleName = Optional.ofNullable(req.getRole()).orElse("STUDENT");
    User user =
        User.builder()
            .username(username)
            .password(req.getPassword())
            .role(User.Role.valueOf(roleName.toUpperCase()))
            .build();
    userService.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
