package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.repository.GroupRepository;
import com.example.scheduletracker.service.impl.GroupServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

  @Mock private GroupRepository repo;

  private GroupService service;

  @BeforeEach
  void setup() {
    service = new GroupServiceImpl(repo);
  }

  @Test
  void saveDelegatesToRepo() {
    Group g = new Group();
    g.setName("G1");
    when(repo.save(any(Group.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Group saved = service.save(g);

    assertEquals("G1", saved.getName());
    verify(repo).save(g);
  }

  @Test
  void findByIdReturnsData() {
    java.util.UUID gid = java.util.UUID.randomUUID();
    Group g = new Group(gid, "G1", null);
    when(repo.findById(gid)).thenReturn(Optional.of(g));

    Optional<Group> result = service.findById(gid);

    assertTrue(result.isPresent());
    assertEquals(gid, result.get().getId());
  }

  @Test
  void findAllReturnsList() {
    when(repo.findAll()).thenReturn(List.of(new Group()));

    List<Group> result = service.findAll();

    assertEquals(1, result.size());
    verify(repo).findAll();
  }
}
