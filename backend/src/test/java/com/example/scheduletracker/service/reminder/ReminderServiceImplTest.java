package com.example.scheduletracker.service.reminder;

import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.entity.NotificationTemplate;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.service.NotificationService;
import com.example.scheduletracker.service.NotificationTemplateService;
import com.example.scheduletracker.service.impl.ReminderServiceImpl;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReminderServiceImplTest {

  @Mock private LessonRepository lessonRepository;
  @Mock private NotificationService notificationService;
  @Mock private NotificationTemplateService templateService;

  private ReminderServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new ReminderServiceImpl(lessonRepository, notificationService, templateService);
  }

  @Test
  void processRemindersSendsNotifications() {
    OffsetDateTime now = OffsetDateTime.now();
    Lesson lesson =
        new Lesson(
            java.util.UUID.randomUUID(),
            now.plusMinutes(10),
            60,
            Lesson.Status.SCHEDULED,
            null,
            Group.builder().description("user@example.com").build());
    when(lessonRepository.findByDateTimeBetween(any(), any())).thenReturn(List.of(lesson));
    when(templateService.findByCodeAndLang(any(), any()))
        .thenReturn(
            java.util.Optional.of(
                new NotificationTemplate(java.util.UUID.randomUUID(), "reminder", "en", "sub", "body {{time}}")));

    service.processReminders();

    verify(notificationService).sendEmail(eq("user@example.com"), eq("sub"), contains("body"));
  }
}
