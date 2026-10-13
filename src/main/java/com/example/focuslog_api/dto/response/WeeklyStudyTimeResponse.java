package com.example.focuslog_api.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "WeeklyStudyTimeResponse", description = "Total study time for one ISO week (Monday to Sunday)")
public record WeeklyStudyTimeResponse(

    @Schema(example = "2026-10-12", description = "Monday of the week")
    LocalDate weekStart,

    @Schema(example = "2026-10-18", description = "Sunday of the week")
    LocalDate weekEnd,

    @Schema(example = "3600000")
    long totalMs,

    @Schema(example = "60.0")
    double totalMinutes

) {}
