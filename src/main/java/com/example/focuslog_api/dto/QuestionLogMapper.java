package com.example.focuslog_api.dto;

import com.example.focuslog_api.dto.response.QuestionLogResponse;
import com.example.focuslog_api.model.QuestionLog;

public class QuestionLogMapper {

    private QuestionLogMapper() {}

    public static QuestionLogResponse toResponse(QuestionLog log) {
        return new QuestionLogResponse(
            log.getId(),
            log.getTotalQuestions(),
            log.getCorrectAnswers(),
            log.getWrongAnswers(),
            log.getTopicId(),
            log.getSubjectId(),
            log.getUserId(),
            log.getCreatedAt()
        );
    }
}
