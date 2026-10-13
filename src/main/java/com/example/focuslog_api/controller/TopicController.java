package com.example.focuslog_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.focuslog_api.dto.TopicMapper;
import com.example.focuslog_api.dto.request.NameRequest;
import com.example.focuslog_api.dto.request.TopicRequest;
import com.example.focuslog_api.dto.response.TopicResponse;
import com.example.focuslog_api.service.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/topics")
@Tag(name = "Topics", description = "Specific topics inside a subject")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a topic")
    public TopicResponse createTopic(@Valid @RequestBody TopicRequest request) {
        return TopicMapper.toResponse(topicService.createTopic(request));
    }

    @GetMapping
    @Operation(summary = "List all topics")
    public List<TopicResponse> getAllTopics() {
        return topicService.getAllTopics().stream().map(TopicMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a topic by id")
    public TopicResponse getTopicById(@PathVariable Long id) {
        return TopicMapper.toResponse(topicService.getTopicById(id));
    }

    @GetMapping("/by-subject/{subjectId}")
    @Operation(summary = "List the topics of a subject")
    public List<TopicResponse> getTopicsBySubjectId(@PathVariable Long subjectId) {
        return topicService.getTopicsBySubjectId(subjectId).stream().map(TopicMapper::toResponse).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Rename a topic")
    public TopicResponse updateTopic(@PathVariable Long id, @Valid @RequestBody NameRequest request) {
        return TopicMapper.toResponse(topicService.updateTopic(id, request.name()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a topic", description = "Returns the topic that was deleted")
    public TopicResponse deleteTopic(@PathVariable Long id) {
        return TopicMapper.toResponse(topicService.deleteTopic(id));
    }
}
