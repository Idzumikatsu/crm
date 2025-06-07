package com.example.scheduletracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Payload for user registration. */
public record SignupRequest(
    @NotBlank String username,
    @NotBlank String password,
    @Size(min = 1) String role) {}
