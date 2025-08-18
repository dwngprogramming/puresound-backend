package com.puresound.backend.dto.listener;

import com.puresound.backend.constant.Status;

import java.time.Instant;
import java.time.LocalDate;

public record ListenerResponse(
    String id,
    String username,
    String email,
    String firstName,
    String lastName,
    String phone,
    String gender,
    LocalDate dob,
    String avatar,
    Instant lastLoginAt,
    Status status,
    Instant createdAt,
    Instant updatedAt
) {
}
