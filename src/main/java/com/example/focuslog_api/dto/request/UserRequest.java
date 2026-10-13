package com.example.focuslog_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "UserRequest", description = "Payload to register a new user")
public record UserRequest(

    @Schema(example = "Ada Lovelace")
    @NotBlank(message = "name is required")
    String name,

    @Schema(example = "ada@example.com")
    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    String email,

    @Schema(example = "password123")
    @NotBlank(message = "password is required")
    String password

) {}
