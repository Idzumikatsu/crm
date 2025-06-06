package com.example.scheduletracker.entity;

import jakarta.persistence.*;

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

  public TeacherStudent(Teacher teacher, Student student) {
    this.id = new Id(teacher.getId(), student.getId());
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

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Id id;
    private Teacher teacher;
    private Student student;

    public Builder id(Id id) {
      this.id = id;
      return this;
    }

    public Builder teacher(Teacher teacher) {
      this.teacher = teacher;
      return this;
    }

    public Builder student(Student student) {
      this.student = student;
      return this;
    }

    public TeacherStudent build() {
      TeacherStudent ts = new TeacherStudent();
      ts.id = this.id;
      ts.teacher = this.teacher;
      ts.student = this.student;
      return ts;
    }
  }

  @Embeddable
  public static class Id implements java.io.Serializable {
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
      return java.util.Objects.equals(teacherId, id.teacherId) &&
             java.util.Objects.equals(studentId, id.studentId);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(teacherId, studentId);
    }
  }
}
