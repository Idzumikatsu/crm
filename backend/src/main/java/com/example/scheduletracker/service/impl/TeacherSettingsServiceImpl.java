package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.TeacherSettings;
import com.example.scheduletracker.repository.TeacherSettingsRepository;
import com.example.scheduletracker.service.TeacherSettingsService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TeacherSettingsServiceImpl implements TeacherSettingsService {
  private final TeacherSettingsRepository repo;

  public TeacherSettingsServiceImpl(TeacherSettingsRepository repo) {
    this.repo = repo;
  }

  @Override
  public Optional<TeacherSettings> findByTeacherId(UUID teacherId) {
    return repo.findById(teacherId);
  }

  @Override
  public TeacherSettings save(TeacherSettings settings) {
    return repo.save(settings);
  }
}
