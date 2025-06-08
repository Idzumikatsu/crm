package com.example.scheduletracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Credentials provided by a user when logging in. */
public record LoginRequest(
    @NotBlank String username, @NotBlank String password, @Size(min = 6, max = 6) String code) {}
