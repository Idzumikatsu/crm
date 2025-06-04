package com.example.scheduletracker.controller;

import com.example.scheduletracker.config.jwt.JwtUtils;
import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> payload) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.get("username"), payload.get("password"))
        );
        String username = auth.getName();
        User user = userService.findByUsername(username).orElseThrow();
        String token = jwtUtils.generateToken(username, user.getRole().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
