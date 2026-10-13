package com.example.focuslog_api.service;

import java.util.List;

import com.example.focuslog_api.dto.request.QuestionLogRequest;
import com.example.focuslog_api.model.QuestionLog;

public interface QuestionLogService {

    QuestionLog createQuestionLog(QuestionLogRequest request);

    QuestionLog getQuestionLogById(Long id);

    List<QuestionLog> getAllQuestionLogs();

    List<QuestionLog> getQuestionLogsBySubjectId(Long subjectId);

    List<QuestionLog> getQuestionLogsByTopicId(Long topicId);
}
