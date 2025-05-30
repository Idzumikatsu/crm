// GroupRepository.java
package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> { }
