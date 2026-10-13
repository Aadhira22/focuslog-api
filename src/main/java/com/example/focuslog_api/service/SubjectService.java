package com.example.focuslog_api.service;

import java.util.List;

import com.example.focuslog_api.dto.request.SubjectRequest;
import com.example.focuslog_api.model.Subject;

public interface SubjectService {

    Subject createSubject(SubjectRequest request);

    List<Subject> getAllSubjects();

    Subject getSubjectById(Long id);

    List<Subject> getSubjectsByUserId(Long userId);

    Subject updateSubject(Long id, String name);

    Subject deleteSubject(Long id);
}
