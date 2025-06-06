// src/main/java/com/example/scheduletracker/controller/UserController.java
package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.dto.UserDto;
import com.example.scheduletracker.mapper.UserMapper;
import com.example.scheduletracker.service.UserService;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService svc;
  private final UserMapper mapper;

  public UserController(UserService svc, UserMapper mapper) {
    this.svc = svc;
    this.mapper = mapper;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> currentUser(org.springframework.security.core.Authentication auth) {
    String username = auth.getName();
    return svc.findByUsername(username)
        .map(mapper::toDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{username}")
  public ResponseEntity<UserDto> getByUsername(@PathVariable String username) {
    Optional<User> user = svc.findByUsername(username);
    return user.map(mapper::toDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public UserDto create(@RequestBody UserDto user) {
    User saved = svc.save(mapper.toEntity(user));
    return mapper.toDto(saved);
  }
}
