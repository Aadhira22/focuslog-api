package com.example.focuslog_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserResponse", description = "Public view of a user (never includes the password)")
public record UserResponse(
    Long id,
    String name,
    String email
) {}
