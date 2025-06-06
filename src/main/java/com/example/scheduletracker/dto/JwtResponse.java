package com.example.scheduletracker.dto;

/** Simple DTO returned after successful authentication. */
public record JwtResponse(String token, String username, String role) {}
