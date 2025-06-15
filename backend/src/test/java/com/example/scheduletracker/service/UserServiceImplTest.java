package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.repository.UserRepository;
import com.example.scheduletracker.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepository repo;

  private UserServiceImpl service;

  @BeforeEach
  void setup() {
    service = new UserServiceImpl(repo);
  }

  @Test
  void saveStoresPasswordAsIs() {
    when(repo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    User u = new User(null, "alice", "secret", User.Role.STUDENT, null, false, true);

    User saved = service.save(u);

    assertEquals("secret", saved.getPassword());
  }
}
