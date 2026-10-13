package com.example.focuslog_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.focuslog_api.dto.QuestionLogMapper;
import com.example.focuslog_api.dto.request.QuestionLogRequest;
import com.example.focuslog_api.dto.response.QuestionLogResponse;
import com.example.focuslog_api.service.QuestionLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/question-logs")
@Tag(name = "Question logs", description = "Practice sessions with correct/wrong counts")
public class QuestionLogController {

    private final QuestionLogService questionLogService;

    public QuestionLogController(QuestionLogService questionLogService) {
        this.questionLogService = questionLogService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Log a practice session", description = "Returns 400 if correctAnswers + wrongAnswers is greater than totalQuestions")
    public QuestionLogResponse createQuestionLog(@Valid @RequestBody QuestionLogRequest request) {
        return QuestionLogMapper.toResponse(questionLogService.createQuestionLog(request));
    }

    @GetMapping
    @Operation(summary = "List all question logs")
    public List<QuestionLogResponse> getAllQuestionLogs() {
        return questionLogService.getAllQuestionLogs().stream().map(QuestionLogMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a question log by id")
    public QuestionLogResponse getQuestionLogById(@PathVariable Long id) {
        return QuestionLogMapper.toResponse(questionLogService.getQuestionLogById(id));
    }

    @GetMapping("/by-subject/{subjectId}")
    @Operation(summary = "List the question logs of a subject")
    public List<QuestionLogResponse> getBySubjectId(@PathVariable Long subjectId) {
        return questionLogService.getQuestionLogsBySubjectId(subjectId).stream().map(QuestionLogMapper::toResponse).toList();
    }

    @GetMapping("/by-topic/{topicId}")
    @Operation(summary = "List the question logs of a topic")
    public List<QuestionLogResponse> getByTopicId(@PathVariable Long topicId) {
        return questionLogService.getQuestionLogsByTopicId(topicId).stream().map(QuestionLogMapper::toResponse).toList();
    }
}
