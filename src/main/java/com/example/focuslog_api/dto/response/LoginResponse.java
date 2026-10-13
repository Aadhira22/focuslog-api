package com.example.focuslog_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "Result of a successful login")
public record LoginResponse(

    @Schema(example = "Login successful")
    String message,

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
    String token

) {}
