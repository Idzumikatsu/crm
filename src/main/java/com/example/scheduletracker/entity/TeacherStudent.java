package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher_students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherStudent {
    @EmbeddedId
    private Id id;

    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Id implements java.io.Serializable {
        @Column(name = "teacher_id")
        private Long teacherId;
        @Column(name = "student_id")
        private Long studentId;
    }
}
