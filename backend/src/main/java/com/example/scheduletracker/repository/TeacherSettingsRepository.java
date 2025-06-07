package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.TeacherSettings;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherSettingsRepository extends JpaRepository<TeacherSettings, UUID> {}
