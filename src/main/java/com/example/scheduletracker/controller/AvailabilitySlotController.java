package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.AvailabilitySlot;
import com.example.scheduletracker.service.AvailabilitySlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilitySlotController {
    private final AvailabilitySlotService svc;

    @GetMapping
    public List<AvailabilitySlot> all(@RequestParam(required = false) Long teacherId) {
        if (teacherId != null) {
            return svc.findByTeacherId(teacherId);
        }
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilitySlot> get(@PathVariable Long id) {
        return svc.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AvailabilitySlot create(@RequestBody AvailabilitySlot slot) {
        return svc.save(slot);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilitySlot> update(@PathVariable Long id, @RequestBody AvailabilitySlot slot) {
        return svc.findById(id)
                .map(existing -> {
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
