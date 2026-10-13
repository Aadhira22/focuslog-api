package com.example.focuslog_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "StudySessionRequest", description = "Payload to log time spent studying")
public record StudySessionRequest(

    @Schema(example = "1800000", description = "Time studied, in milliseconds (1800000 = 30 minutes)")
    @NotNull(message = "durationMs is required")
    @Positive(message = "durationMs must be greater than zero")
    Long durationMs,

    @Schema(example = "1")
    @NotNull(message = "topicId is required")
    Long topicId,

    @Schema(example = "1")
    @NotNull(message = "subjectId is required")
    Long subjectId

) {}
