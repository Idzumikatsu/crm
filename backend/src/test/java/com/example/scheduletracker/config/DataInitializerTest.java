package com.example.scheduletracker.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

  @Mock private UserRepository repo;
  @Mock private PasswordEncoder encoder;
  @Mock private com.example.scheduletracker.service.security.TotpService totpService;

  private DataInitializer initializer;

  @BeforeEach
  void setup() {
    initializer = new DataInitializer(repo, encoder, totpService);
  }

  @Test
  void createsDefaultUsersWhenEmpty() {
    when(repo.findByUsername("manager")).thenReturn(java.util.Optional.empty());
    when(repo.findByUsername("teacher")).thenReturn(java.util.Optional.empty());
    when(repo.findByUsername("admin")).thenReturn(java.util.Optional.empty());
    when(encoder.encode(any())).thenReturn("enc");
    initializer.run(null);
    verify(repo, times(3)).save(any(User.class));
  }

  @Test
  void doesNothingWhenUsersExist() {
    when(repo.findByUsername(anyString())).thenReturn(java.util.Optional.of(new User()));
    initializer.run(null);
    verify(repo, never()).save(any());
  }
}
