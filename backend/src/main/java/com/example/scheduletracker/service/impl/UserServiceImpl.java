// src/main/java/com/example/scheduletracker/service/impl/UserServiceImpl.java
package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.repository.UserRepository;
import com.example.scheduletracker.service.UserService;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repo;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder) {
    this.repo = repo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User save(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return repo.save(user);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return repo.findByUsername(username);
  }
}
