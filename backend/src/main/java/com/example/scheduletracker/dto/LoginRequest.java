package com.example.scheduletracker.dto;

/** Credentials provided by a user when logging in. */
public record LoginRequest(String username, String password, String code) {}
