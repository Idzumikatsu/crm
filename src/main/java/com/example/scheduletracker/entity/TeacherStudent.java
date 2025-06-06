package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/** Assignment of teacher to student. */
@Entity
@Table(name = "teacher_students")
public class TeacherStudent {
  @EmbeddedId private Id id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("teacherId")
  @JoinColumn(name = "teacher_id", nullable = false)
  private Teacher teacher;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("studentId")
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  public TeacherStudent() {}

  public TeacherStudent(Id id, Teacher teacher, Student student) {
    this.id = id;
    this.teacher = teacher;
    this.student = student;
  }

  public Id getId() {
    return id;
  }

  public void setId(Id id) {
    this.id = id;
  }

  public Teacher getTeacher() {
    return teacher;
  }

  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  @Embeddable
  public static class Id implements Serializable {
    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "student_id")
    private Long studentId;

    public Id() {}

    public Id(Long teacherId, Long studentId) {
      this.teacherId = teacherId;
      this.studentId = studentId;
    }

    public Long getTeacherId() {
      return teacherId;
    }

    public void setTeacherId(Long teacherId) {
      this.teacherId = teacherId;
    }

    public Long getStudentId() {
      return studentId;
    }

    public void setStudentId(Long studentId) {
      this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Id id = (Id) o;
      return Objects.equals(teacherId, id.teacherId) && Objects.equals(studentId, id.studentId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(teacherId, studentId);
    }
  }
}
