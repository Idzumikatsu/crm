package com.example.scheduletracker.config;

import static org.mockito.ArgumentMatchers.any;
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

  private DataInitializer initializer;

  @BeforeEach
  void setup() {
    initializer = new DataInitializer(repo, encoder);
  }

  @Test
  void createsDefaultUsersWhenEmpty() {
    when(repo.count()).thenReturn(0L);
    when(encoder.encode(any())).thenReturn("enc");
    initializer.run(null);
    verify(repo, times(2)).save(any(User.class));
  }

  @Test
  void doesNothingWhenUsersExist() {
    when(repo.count()).thenReturn(1L);
    initializer.run(null);
    verify(repo, never()).save(any());
  }
}
