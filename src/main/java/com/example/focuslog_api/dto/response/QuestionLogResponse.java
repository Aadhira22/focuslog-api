package com.example.focuslog_api.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "QuestionLogResponse")
public record QuestionLogResponse(
    Long id,
    Long totalQuestions,
    Long correctAnswers,
    Long wrongAnswers,
    Long topicId,
    Long subjectId,
    Long userId,
    LocalDateTime createdAt
) {}
