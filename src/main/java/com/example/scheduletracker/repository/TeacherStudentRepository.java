package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.TeacherStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherStudentRepository
    extends JpaRepository<TeacherStudent, TeacherStudent.Id> {}
