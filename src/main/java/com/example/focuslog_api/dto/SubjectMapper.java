package com.example.focuslog_api.dto;

import com.example.focuslog_api.dto.response.SubjectResponse;
import com.example.focuslog_api.model.Subject;

public class SubjectMapper {

    private SubjectMapper() {}

    public static SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(subject.getId(), subject.getName(), subject.getUserId());
    }
}
