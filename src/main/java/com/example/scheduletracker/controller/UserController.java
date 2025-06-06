// src/main/java/com/example/scheduletracker/controller/UserController.java
package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService svc;

  @GetMapping("/me")
  public ResponseEntity<User> currentUser(org.springframework.security.core.Authentication auth) {
    String username = auth.getName();
    return svc.findByUsername(username)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> getByUsername(@PathVariable String username) {
    Optional<User> user = svc.findByUsername(username);
    return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public User create(@RequestBody User user) {
    return svc.save(user);
  }
}
