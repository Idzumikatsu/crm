package com.example.scheduletracker.dto;

/** Lightweight representation of a user */
import java.util.UUID;

public record UserDto(UUID id, String username, String role) {}
