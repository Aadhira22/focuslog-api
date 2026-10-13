package com.example.focuslog_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.focuslog_api.dto.request.StudySessionRequest;
import com.example.focuslog_api.exception.ResourceNotFoundException;
import com.example.focuslog_api.model.StudySession;
import com.example.focuslog_api.repository.StudySessionRepository;
import com.example.focuslog_api.service.StudySessionService;

@Service
public class StudySessionServiceImpl implements StudySessionService {

    private final StudySessionRepository studySessionRepository;

    public StudySessionServiceImpl(StudySessionRepository studySessionRepository) {
        this.studySessionRepository = studySessionRepository;
    }

    @Override
    public StudySession createStudySession(StudySessionRequest request) {
        StudySession session = new StudySession();
        session.setDurationMs(request.durationMs());
        session.setTopicId(request.topicId());
        session.setSubjectId(request.subjectId());
        return studySessionRepository.save(session);
    }

    @Override
    public StudySession getStudySessionById(Long id) {
        return studySessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study session not found with id: " + id));
    }

    @Override
    public List<StudySession> getAllStudySessions() {
        return studySessionRepository.findAll();
    }

    @Override
    public List<StudySession> getStudySessionsBySubjectId(Long subjectId) {
        return studySessionRepository.findBySubjectId(subjectId);
    }

    @Override
    public List<StudySession> getStudySessionsByTopicId(Long topicId) {
        return studySessionRepository.findByTopicId(topicId);
    }

    @Override
    public long getTotalDurationMsBySubjectId(Long subjectId) {
        return sum(studySessionRepository.findBySubjectId(subjectId));
    }

    @Override
    public long getTotalDurationMsByTopicId(Long topicId) {
        return sum(studySessionRepository.findByTopicId(topicId));
    }

    private long sum(List<StudySession> sessions) {
        return sessions.stream().mapToLong(StudySession::getDurationMs).sum();
    }
}
