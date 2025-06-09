// src/main/java/com/example/scheduletracker/service/UserService.java
package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.User;
import java.util.Optional;

public interface UserService {
  User save(User user);

  Optional<User> findByUsername(String username);

  User update(User user);
}
