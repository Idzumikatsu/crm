package com.example.scheduletracker.controller;

import com.example.scheduletracker.service.AnalyticsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
  private final AnalyticsService analyticsService;

  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @GetMapping(value = "/lessons-per-teacher", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> lessonsPerTeacher() {
    return ResponseEntity.ok(analyticsService.lessonCountPerTeacher());
  }

  @GetMapping(
      value = "/export",
      produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  public ResponseEntity<byte[]> exportExcel() {
    byte[] bytes = analyticsService.exportLessonCountExcel();
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lesson-summary.xlsx")
        .body(bytes);
  }
}
