package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.service.NotificationService;
import com.example.scheduletracker.service.NotificationTemplateService;
import com.example.scheduletracker.service.ReminderService;
import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReminderServiceImpl implements ReminderService {
  private static final Logger log = LoggerFactory.getLogger(ReminderServiceImpl.class);
  private final LessonRepository lessonRepository;
  private final NotificationService notificationService;
  private final NotificationTemplateService templateService;

  public ReminderServiceImpl(
      LessonRepository lessonRepository,
      NotificationService notificationService,
      NotificationTemplateService templateService) {
    this.lessonRepository = lessonRepository;
    this.notificationService = notificationService;
    this.templateService = templateService;
  }

  @Override
  @Scheduled(fixedRate = 60000)
  public void processReminders() {
    OffsetDateTime now = OffsetDateTime.now();
    OffsetDateTime until = now.plusMinutes(30);
    List<Lesson> upcoming = lessonRepository.findByDateTimeBetween(now, until);
    for (Lesson lesson : upcoming) {
      sendReminder(lesson);
    }
  }

  private void sendReminder(Lesson lesson) {
    var studentEmail = lesson.getGroup().getDescription();
    String subject = "Lesson reminder";
    String body = "Lesson at " + lesson.getDateTime();
    var tpl = templateService.findByCodeAndLang("reminder", "en");
    if (tpl.isPresent()) {
      var t = tpl.get();
      if (t.getSubject() != null) subject = t.getSubject();
      if (t.getBodyHtml() != null) {
        body = t.getBodyHtml().replace("{{time}}", lesson.getDateTime().toString());
      }
    }
    if (studentEmail != null && !studentEmail.isBlank()) {
      notificationService.sendEmail(studentEmail, subject, body);
    } else {
      log.info("No student email for lesson {}", lesson.getId());
    }
  }
}
