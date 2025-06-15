// src/main/java/com/example/scheduletracker/service/impl/UserServiceImpl.java
package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.repository.UserRepository;
import com.example.scheduletracker.service.UserService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repo;

  public UserServiceImpl(UserRepository repo) {
    this.repo = repo;
  }

  @Override
  public User save(User user) {
    return repo.save(user);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return repo.findByUsername(username);
  }

  @Override
  public User update(User user) {
    return repo.save(user);
  }
}
