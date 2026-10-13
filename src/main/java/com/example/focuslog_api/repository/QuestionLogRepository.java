package com.example.focuslog_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.focuslog_api.model.QuestionLog;

public interface QuestionLogRepository extends JpaRepository<QuestionLog, Long> {

    List<QuestionLog> findBySubjectId(Long subjectId);

    List<QuestionLog> findByTopicId(Long topicId);

    List<QuestionLog> findByUserId(Long userId);
}
