package com.example.focuslog_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "QuestionLogRequest", description = "Payload to log a practice session")
public record QuestionLogRequest(

    @Schema(example = "10", description = "Questions answered in the session")
    @NotNull(message = "totalQuestions is required")
    @PositiveOrZero(message = "totalQuestions cannot be negative")
    Long totalQuestions,

    @Schema(example = "8")
    @NotNull(message = "correctAnswers is required")
    @PositiveOrZero(message = "correctAnswers cannot be negative")
    Long correctAnswers,

    @Schema(example = "2")
    @NotNull(message = "wrongAnswers is required")
    @PositiveOrZero(message = "wrongAnswers cannot be negative")
    Long wrongAnswers,

    @Schema(example = "1")
    @NotNull(message = "topicId is required")
    Long topicId,

    @Schema(example = "1")
    @NotNull(message = "subjectId is required")
    Long subjectId,

    @Schema(example = "1")
    @NotNull(message = "userId is required")
    Long userId

) {}
