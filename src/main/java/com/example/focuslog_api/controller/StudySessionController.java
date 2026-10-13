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

import com.example.focuslog_api.dto.StudySessionMapper;
import com.example.focuslog_api.dto.request.StudySessionRequest;
import com.example.focuslog_api.dto.response.StudySessionResponse;
import com.example.focuslog_api.service.StudySessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/study-sessions")
@Tag(name = "Study sessions", description = "Time spent studying, in milliseconds")
public class StudySessionController {

    private final StudySessionService studySessionService;

    public StudySessionController(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Log a study session", description = "durationMs is in milliseconds, e.g. 1800000 = 30 minutes")
    public StudySessionResponse createStudySession(@Valid @RequestBody StudySessionRequest request) {
        return StudySessionMapper.toResponse(studySessionService.createStudySession(request));
    }

    @GetMapping
    @Operation(summary = "List all study sessions")
    public List<StudySessionResponse> getAllStudySessions() {
        return studySessionService.getAllStudySessions().stream().map(StudySessionMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a study session by id")
    public StudySessionResponse getStudySessionById(@PathVariable Long id) {
        return StudySessionMapper.toResponse(studySessionService.getStudySessionById(id));
    }

    @GetMapping("/by-subject/{subjectId}")
    @Operation(summary = "List the study sessions of a subject")
    public List<StudySessionResponse> getBySubjectId(@PathVariable Long subjectId) {
        return studySessionService.getStudySessionsBySubjectId(subjectId).stream().map(StudySessionMapper::toResponse).toList();
    }

    @GetMapping("/by-topic/{topicId}")
    @Operation(summary = "List the study sessions of a topic")
    public List<StudySessionResponse> getByTopicId(@PathVariable Long topicId) {
        return studySessionService.getStudySessionsByTopicId(topicId).stream().map(StudySessionMapper::toResponse).toList();
    }
}
