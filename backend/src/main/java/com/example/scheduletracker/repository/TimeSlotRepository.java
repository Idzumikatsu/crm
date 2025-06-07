package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TimeSlot;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {
  List<TimeSlot> findByTeacher(Teacher teacher);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select t from TimeSlot t where t.teacher.id = :teacherId and t.startTs <= :start and t.endTs >= :end")
  Optional<TimeSlot> findSlotForPeriodLocked(
      @Param("teacherId") UUID teacherId,
      @Param("start") OffsetDateTime start,
      @Param("end") OffsetDateTime end);
}
