// GroupRepository.java
package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Group;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, UUID> {}
