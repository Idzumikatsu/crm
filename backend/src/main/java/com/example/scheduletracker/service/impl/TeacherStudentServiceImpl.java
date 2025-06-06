package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.TeacherStudent;
import com.example.scheduletracker.repository.TeacherStudentRepository;
import com.example.scheduletracker.service.TeacherStudentService;
import org.springframework.stereotype.Service;

@Service
public class TeacherStudentServiceImpl implements TeacherStudentService {
  private final TeacherStudentRepository repo;

  public TeacherStudentServiceImpl(TeacherStudentRepository repo) {
    this.repo = repo;
  }

  @Override
  public TeacherStudent save(TeacherStudent ts) {
    return repo.save(ts);
  }
}
