package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.service.TimeSlotService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/time-slots")
@RequiredArgsConstructor
public class TimeSlotController {
  private final TimeSlotService svc;

  @GetMapping
  public List<TimeSlot> all(@RequestParam(required = false) Long teacherId) {
    if (teacherId != null) {
      return svc.findByTeacherId(teacherId);
    }
    return svc.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<TimeSlot> get(@PathVariable Long id) {
    return svc.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public TimeSlot create(@RequestBody TimeSlot slot) {
    return svc.save(slot);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TimeSlot> update(@PathVariable Long id, @RequestBody TimeSlot slot) {
    return svc.findById(id)
        .map(
            existing -> {
              slot.setId(id);
              return ResponseEntity.ok(svc.save(slot));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    svc.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
