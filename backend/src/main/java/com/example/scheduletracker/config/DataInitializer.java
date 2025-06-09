package com.example.scheduletracker.config;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.repository.UserRepository;
import com.example.scheduletracker.service.security.TotpService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataInitializer implements ApplicationRunner {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TotpService totpService;

  public DataInitializer(
      UserRepository userRepository, PasswordEncoder passwordEncoder, TotpService totpService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.totpService = totpService;
  }

  private void createIfMissing(String username, String rawPassword, User.Role role) {
    if (userRepository.findByUsername(username).isEmpty()) {
      User user =
          new User(
              null,
              username,
              passwordEncoder.encode(rawPassword),
              role,
              totpService.generateSecret(),
              false);
      userRepository.save(user);
    }
  }

  @Override
  public void run(ApplicationArguments args) {
    createIfMissing("manager", "manager", User.Role.MANAGER);
    createIfMissing("teacher", "teacher", User.Role.TEACHER);
    createIfMissing("admin", "admin", User.Role.ADMIN);
  }
}
