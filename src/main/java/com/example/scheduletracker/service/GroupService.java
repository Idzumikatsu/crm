// src/main/java/com/example/scheduletracker/service/GroupService.java
package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Group;
import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group save(Group group);
    List<Group> findAll();
    Optional<Group> findById(Long id);
    void deleteById(Long id);
}
