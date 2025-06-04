package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.repository.GroupRepository;
import com.example.scheduletracker.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository repo;

    private GroupService service;

    @BeforeEach
    void setup() {
        service = new GroupServiceImpl(repo);
    }

    @Test
    void saveDelegatesToRepo() {
        Group g = Group.builder().name("G1").build();
        when(repo.save(any(Group.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Group saved = service.save(g);

        assertEquals("G1", saved.getName());
        verify(repo).save(g);
    }

    @Test
    void findByIdReturnsData() {
        Group g = Group.builder().id(1L).name("G1").build();
        when(repo.findById(1L)).thenReturn(Optional.of(g));

        Optional<Group> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findAllReturnsList() {
        when(repo.findAll()).thenReturn(List.of(Group.builder().build()));

        List<Group> result = service.findAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }
}
