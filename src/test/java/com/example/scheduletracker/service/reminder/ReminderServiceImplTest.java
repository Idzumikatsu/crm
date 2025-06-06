package com.example.scheduletracker.service.reminder;

import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.service.NotificationService;
import com.example.scheduletracker.service.impl.ReminderServiceImpl;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ReminderServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private NotificationService notificationService;

    private ReminderServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReminderServiceImpl(lessonRepository, notificationService);
    }

    @Test
    void processRemindersSendsNotifications() {
        OffsetDateTime now = OffsetDateTime.now();
        Lesson lesson = new Lesson(1L, now.plusMinutes(10), 60, Lesson.Status.SCHEDULED,
                null, Group.builder().description("user@example.com").build());
        when(lessonRepository.findByDateTimeBetween(any(), any())).thenReturn(List.of(lesson));

        service.processReminders();

        verify(notificationService).sendEmail(eq("user@example.com"), any(), contains("Lesson"));
    }
}
