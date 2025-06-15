package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.NotificationTemplate;
import com.example.scheduletracker.service.NotificationTemplateService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {
  private final NotificationTemplateService service;

  public TemplateController(NotificationTemplateService service) {
    this.service = service;
  }

  @GetMapping
  public List<NotificationTemplate> all() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<NotificationTemplate> get(@PathVariable UUID id) {
    return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public NotificationTemplate create(@RequestBody NotificationTemplate t) {
    return service.save(t);
  }

  @PutMapping("/{id}")
  public ResponseEntity<NotificationTemplate> update(
      @PathVariable UUID id, @RequestBody NotificationTemplate t) {
    return service
        .findById(id)
        .map(
            existing -> {
              t.setId(id);
              return ResponseEntity.ok(service.save(t));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
