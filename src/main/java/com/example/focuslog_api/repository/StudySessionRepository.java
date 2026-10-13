package com.example.focuslog_api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.focuslog_api.model.StudySession;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findBySubjectId(Long subjectId);

    List<StudySession> findByTopicId(Long topicId);

    List<StudySession> findBySubjectIdIn(Collection<Long> subjectIds);
}
