package com.example.focuslog_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SubjectResponse")
public record SubjectResponse(
    Long id,
    String name,
    Long userId
) {}
