package com.example.focuslog_api.service;

import java.util.List;

import com.example.focuslog_api.dto.request.StudySessionRequest;
import com.example.focuslog_api.model.StudySession;

public interface StudySessionService {

    StudySession createStudySession(StudySessionRequest request);

    StudySession getStudySessionById(Long id);

    List<StudySession> getAllStudySessions();

    List<StudySession> getStudySessionsBySubjectId(Long subjectId);

    List<StudySession> getStudySessionsByTopicId(Long topicId);

    /** Sum of all study time for a subject, in milliseconds. */
    long getTotalDurationMsBySubjectId(Long subjectId);

    /** Sum of all study time for a topic, in milliseconds. */
    long getTotalDurationMsByTopicId(Long topicId);
}
