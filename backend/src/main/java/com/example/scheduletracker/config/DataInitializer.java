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

  @Override
  public void run(ApplicationArguments args) {
    if (userRepository.count() == 0) {
      User manager =
          new User(
              null,
              "manager",
              passwordEncoder.encode("manager"),
              User.Role.MANAGER,
              totpService.generateSecret());
      userRepository.save(manager);

      User teacher =
          new User(
              null,
              "teacher",
              passwordEncoder.encode("teacher"),
              User.Role.TEACHER,
              totpService.generateSecret());
      userRepository.save(teacher);
    }
  }
}
