package com.example.focuslog_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "SubjectRequest", description = "Payload to create a subject")
public record SubjectRequest(

    @Schema(example = "Java and Spring Boot")
    @NotBlank(message = "name is required")
    String name,

    @Schema(example = "1")
    @NotNull(message = "userId is required")
    Long userId

) {}
