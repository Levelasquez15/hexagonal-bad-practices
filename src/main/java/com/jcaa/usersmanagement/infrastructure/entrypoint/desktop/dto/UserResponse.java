package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserResponse(
    String id,
    String name,
    String email,
    String role,
    String status
) {}
