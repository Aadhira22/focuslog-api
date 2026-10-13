package com.example.focuslog_api.dto.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Error body returned for any failed request")
public record ErrorResponse(
    int status,
    String error,
    String message,
    Instant timestamp
) {}
