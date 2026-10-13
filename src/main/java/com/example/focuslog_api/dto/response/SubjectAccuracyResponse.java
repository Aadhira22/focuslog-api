package com.example.focuslog_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SubjectAccuracyResponse", description = "Answer accuracy for one subject, summed over all logged practice sessions")
public record SubjectAccuracyResponse(

    Long subjectId,
    String subjectName,

    @Schema(example = "30", description = "Total questions answered in this subject")
    long totalQuestions,

    @Schema(example = "20")
    long correctAnswers,

    @Schema(example = "66.67", description = "correctAnswers / totalQuestions * 100, rounded to 2 decimals. null when no questions were logged yet (0% would wrongly mean 'all wrong').")
    Double accuracyPercent

) {}
