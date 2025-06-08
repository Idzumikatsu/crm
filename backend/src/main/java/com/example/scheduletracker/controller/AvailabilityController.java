package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.AvailabilityTemplate;
import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.service.AvailabilityService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AvailabilityController {
  private final AvailabilityService service;

  public AvailabilityController(AvailabilityService service) {
    this.service = service;
  }

  @GetMapping("/templates")
  public List<AvailabilityTemplate> templates(@RequestParam(required = false) UUID teacherId) {
    return service.findTemplates(teacherId);
  }

  @PostMapping("/templates")
  public AvailabilityTemplate createTemplate(@RequestBody AvailabilityTemplate tpl) {
    return service.saveTemplate(tpl);
  }

  @DeleteMapping("/templates/{id}")
  public void deleteTemplate(@PathVariable UUID id) {
    service.deleteTemplate(id);
  }

  @PostMapping("/slots/generate")
  public List<TimeSlot> generate(
      @RequestParam UUID teacherId, @RequestParam LocalDate from, @RequestParam LocalDate to) {
    return service.generateSlots(teacherId, from, to);
  }

  @DeleteMapping("/slots")
  public void delete(@RequestParam UUID teacherId, @RequestParam LocalDate from) {
    service.deleteSlots(teacherId, from);
  }
}
