package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.TeacherSettings;
import java.util.Optional;
import java.util.UUID;

public interface TeacherSettingsService {
  Optional<TeacherSettings> findByTeacherId(UUID teacherId);

  TeacherSettings save(TeacherSettings settings);
}
