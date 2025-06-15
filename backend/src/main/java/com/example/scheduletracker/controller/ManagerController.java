package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Student;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TeacherStudent;
import com.example.scheduletracker.service.StudentService;
import com.example.scheduletracker.service.TeacherService;
import com.example.scheduletracker.service.TeacherStudentService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
  private final TeacherService teacherService;
  private final StudentService studentService;
  private final TeacherStudentService teacherStudentService;

  public ManagerController(
      TeacherService teacherService,
      StudentService studentService,
      TeacherStudentService teacherStudentService) {
    this.teacherService = teacherService;
    this.studentService = studentService;
    this.teacherStudentService = teacherStudentService;
  }

  @GetMapping("/teachers")
  public List<Teacher> teachers() {
    return teacherService.findAll();
  }

  @GetMapping("/students")
  public List<Student> students() {
    return studentService.findAll();
  }

  @PostMapping("/assign")
  public ResponseEntity<TeacherStudent> assign(
      @RequestParam UUID teacherId, @RequestParam UUID studentId) {
    Teacher teacher = teacherService.findById(teacherId).orElse(null);
    Student student = studentService.findById(studentId).orElse(null);
    if (teacher == null || student == null) {
      return ResponseEntity.notFound().build();
    }
    TeacherStudent ts = new TeacherStudent();
    ts.setId(new TeacherStudent.Id(teacherId, studentId));
    ts.setTeacher(teacher);
    ts.setStudent(student);
    return ResponseEntity.ok(teacherStudentService.save(ts));
  }
}
