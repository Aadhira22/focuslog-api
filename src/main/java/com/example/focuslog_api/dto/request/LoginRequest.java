package com.example.focuslog_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest", description = "Credentials used to obtain a JWT")
public record LoginRequest(

    @Schema(example = "demo@focuslog.dev")
    @NotBlank(message = "email is required")
    String email,

    @Schema(example = "password123")
    @NotBlank(message = "password is required")
    String password

) {}
