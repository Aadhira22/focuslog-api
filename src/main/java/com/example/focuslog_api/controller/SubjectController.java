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

import com.example.focuslog_api.dto.SubjectMapper;
import com.example.focuslog_api.dto.request.NameRequest;
import com.example.focuslog_api.dto.request.SubjectRequest;
import com.example.focuslog_api.dto.response.SubjectResponse;
import com.example.focuslog_api.service.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subjects", description = "Broad areas of study")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a subject")
    public SubjectResponse createSubject(@Valid @RequestBody SubjectRequest request) {
        return SubjectMapper.toResponse(subjectService.createSubject(request));
    }

    @GetMapping
    @Operation(summary = "List all subjects")
    public List<SubjectResponse> getAllSubjects() {
        return subjectService.getAllSubjects().stream().map(SubjectMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a subject by id")
    public SubjectResponse getSubjectById(@PathVariable Long id) {
        return SubjectMapper.toResponse(subjectService.getSubjectById(id));
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "List the subjects of a user")
    public List<SubjectResponse> getSubjectsByUserId(@PathVariable Long userId) {
        return subjectService.getSubjectsByUserId(userId).stream().map(SubjectMapper::toResponse).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Rename a subject")
    public SubjectResponse updateSubject(@PathVariable Long id, @Valid @RequestBody NameRequest request) {
        return SubjectMapper.toResponse(subjectService.updateSubject(id, request.name()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a subject", description = "Returns the subject that was deleted")
    public SubjectResponse deleteSubject(@PathVariable Long id) {
        return SubjectMapper.toResponse(subjectService.deleteSubject(id));
    }
}
