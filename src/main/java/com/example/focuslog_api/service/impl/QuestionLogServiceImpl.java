package com.example.focuslog_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.focuslog_api.dto.request.QuestionLogRequest;
import com.example.focuslog_api.exception.BadRequestException;
import com.example.focuslog_api.exception.ResourceNotFoundException;
import com.example.focuslog_api.model.QuestionLog;
import com.example.focuslog_api.repository.QuestionLogRepository;
import com.example.focuslog_api.service.QuestionLogService;

@Service
public class QuestionLogServiceImpl implements QuestionLogService {

    private final QuestionLogRepository questionLogRepository;

    public QuestionLogServiceImpl(QuestionLogRepository questionLogRepository) {
        this.questionLogRepository = questionLogRepository;
    }

    @Override
    public QuestionLog createQuestionLog(QuestionLogRequest request) {
        // Guards the stats: without this, correct > total would produce accuracy above 100%.
        long answered = request.correctAnswers() + request.wrongAnswers();
        if (answered > request.totalQuestions()) {
            throw new BadRequestException("correctAnswers + wrongAnswers (" + answered
                    + ") cannot exceed totalQuestions (" + request.totalQuestions() + ")");
        }

        QuestionLog log = new QuestionLog();
        log.setTotalQuestions(request.totalQuestions());
        log.setCorrectAnswers(request.correctAnswers());
        log.setWrongAnswers(request.wrongAnswers());
        log.setTopicId(request.topicId());
        log.setSubjectId(request.subjectId());
        log.setUserId(request.userId());
        return questionLogRepository.save(log);
    }

    @Override
    public QuestionLog getQuestionLogById(Long id) {
        return questionLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question log not found with id: " + id));
    }

    @Override
    public List<QuestionLog> getAllQuestionLogs() {
        return questionLogRepository.findAll();
    }

    @Override
    public List<QuestionLog> getQuestionLogsBySubjectId(Long subjectId) {
        return questionLogRepository.findBySubjectId(subjectId);
    }

    @Override
    public List<QuestionLog> getQuestionLogsByTopicId(Long topicId) {
        return questionLogRepository.findByTopicId(topicId);
    }
}
