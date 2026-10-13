package com.example.focuslog_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "NameRequest", description = "Payload to rename a subject or topic")
public record NameRequest(

    @Schema(example = "Spring Data JPA")
    @NotBlank(message = "name is required")
    String name

) {}
