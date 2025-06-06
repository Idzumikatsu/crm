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
      User manager = new User();
      manager.setUsername("manager");
      manager.setPassword(passwordEncoder.encode("manager"));
      manager.setRole(User.Role.MANAGER);
      userRepository.save(manager);

      User teacher = new User();
      teacher.setUsername("teacher");
      teacher.setPassword(passwordEncoder.encode("teacher"));
      teacher.setRole(User.Role.TEACHER);
      userRepository.save(teacher);
    }
  }
}
