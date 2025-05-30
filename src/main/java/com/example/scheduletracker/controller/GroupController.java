// src/main/java/com/example/scheduletracker/controller/GroupController.java
package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService svc;

    @GetMapping
    public List<Group> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> get(@PathVariable Long id) {
        return svc.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Group create(@RequestBody Group group) {
        return svc.save(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> update(@PathVariable Long id, @RequestBody Group g) {
        return svc.findById(id)
                .map(existing -> {
                    g.setId(id);
                    return ResponseEntity.ok(svc.save(g));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
