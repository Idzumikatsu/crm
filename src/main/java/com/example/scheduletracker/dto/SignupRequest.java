package com.example.scheduletracker.dto;

/** Payload for user registration. */
public record SignupRequest(String username, String password, String role) {}
