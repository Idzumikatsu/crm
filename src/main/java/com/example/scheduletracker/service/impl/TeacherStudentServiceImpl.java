package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.TeacherStudent;
import com.example.scheduletracker.repository.TeacherStudentRepository;
import com.example.scheduletracker.service.TeacherStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherStudentServiceImpl implements TeacherStudentService {
  private final TeacherStudentRepository repo;

  @Override
  public TeacherStudent save(TeacherStudent ts) {
    return repo.save(ts);
  }
}
