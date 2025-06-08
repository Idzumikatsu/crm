package com.example.scheduletracker.service;

import com.example.scheduletracker.dto.LessonCountDto;
import java.util.List;

public interface AnalyticsService {
  List<LessonCountDto> lessonCountPerTeacher();

  byte[] exportLessonCountExcel();
}
