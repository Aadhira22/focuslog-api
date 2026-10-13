package com.example.focuslog_api.dto;

import com.example.focuslog_api.dto.response.StudySessionResponse;
import com.example.focuslog_api.model.StudySession;

public class StudySessionMapper {

    private StudySessionMapper() {}

    public static StudySessionResponse toResponse(StudySession session) {
        return new StudySessionResponse(
            session.getId(),
            session.getDurationMs(),
            session.getTopicId(),
            session.getSubjectId(),
            session.getCreatedAt()
        );
    }
}
