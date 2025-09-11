package com.puresound.backend.dto.listener;

import com.puresound.backend.constant.Status;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

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
    Instant updatedAt,
    List<ListenerSubResponse> subscriptions
) {
}
