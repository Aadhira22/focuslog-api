package com.example.focuslog_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TopicResponse")
public record TopicResponse(
    Long id,
    String name,
    Long subjectId
) {}
