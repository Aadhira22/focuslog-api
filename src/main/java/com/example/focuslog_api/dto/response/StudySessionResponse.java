package com.example.focuslog_api.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "StudySessionResponse")
public record StudySessionResponse(
    Long id,
    Long durationMs,
    Long topicId,
    Long subjectId,
    LocalDateTime createdAt
) {}
