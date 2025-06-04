package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.Student;
import com.example.scheduletracker.entity.TeacherStudent;
import com.example.scheduletracker.service.TeacherService;
import com.example.scheduletracker.service.StudentService;
import com.example.scheduletracker.service.TeacherStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final TeacherStudentService teacherStudentService;

    @GetMapping("/teachers")
    public List<Teacher> teachers() {
        return teacherService.findAll();
    }

    @GetMapping("/students")
    public List<Student> students() {
        return studentService.findAll();
    }

    @PostMapping("/assign")
    public ResponseEntity<TeacherStudent> assign(@RequestParam Long teacherId, @RequestParam Long studentId) {
        Teacher teacher = teacherService.findById(teacherId).orElse(null);
        Student student = studentService.findById(studentId).orElse(null);
        if (teacher == null || student == null) {
            return ResponseEntity.notFound().build();
        }
        TeacherStudent ts = TeacherStudent.builder()
                .id(new TeacherStudent.Id(teacherId, studentId))
                .teacher(teacher)
                .student(student)
                .build();
        return ResponseEntity.ok(teacherStudentService.save(ts));
    }
}
