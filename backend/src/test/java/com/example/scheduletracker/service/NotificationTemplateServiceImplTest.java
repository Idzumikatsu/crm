package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.NotificationTemplate;
import com.example.scheduletracker.repository.NotificationTemplateRepository;
import com.example.scheduletracker.service.impl.NotificationTemplateServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationTemplateServiceImplTest {

  @Mock private NotificationTemplateRepository repo;

  private NotificationTemplateService service;

  @BeforeEach
  void setup() {
    service = new NotificationTemplateServiceImpl(repo);
  }

  @Test
  void saveDelegatesToRepo() {
    NotificationTemplate t = new NotificationTemplate();
    when(repo.save(any(NotificationTemplate.class))).thenReturn(t);
    NotificationTemplate saved = service.save(t);
    assertSame(t, saved);
    verify(repo).save(t);
  }

  @Test
  void findByCodeAndLangCallsRepo() {
    NotificationTemplate t = new NotificationTemplate(1L, "reminder", "en", null, null);
    when(repo.findByCodeAndLang("reminder", "en")).thenReturn(Optional.of(t));
    Optional<NotificationTemplate> result = service.findByCodeAndLang("reminder", "en");
    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
  }

  @Test
  void findAllReturnsRepoData() {
    when(repo.findAll()).thenReturn(List.of(new NotificationTemplate()));
    List<NotificationTemplate> all = service.findAll();
    assertEquals(1, all.size());
  }
}
