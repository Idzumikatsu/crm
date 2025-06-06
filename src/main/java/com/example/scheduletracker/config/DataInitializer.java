package com.example.scheduletracker.config;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.repository.UserRepository;
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

  public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(ApplicationArguments args) {
    if (userRepository.count() == 0) {
      User manager =
          new User(
              null,
              "manager",
              passwordEncoder.encode("manager"),
              User.Role.MANAGER);
      userRepository.save(manager);

      User teacher =
          new User(
              null,
              "teacher",
              passwordEncoder.encode("teacher"),
              User.Role.TEACHER);
      userRepository.save(teacher);
    }
  }
}
