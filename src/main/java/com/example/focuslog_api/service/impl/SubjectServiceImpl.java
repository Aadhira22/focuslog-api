package com.example.focuslog_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.focuslog_api.dto.request.SubjectRequest;
import com.example.focuslog_api.exception.ResourceNotFoundException;
import com.example.focuslog_api.model.Subject;
import com.example.focuslog_api.repository.SubjectRepository;
import com.example.focuslog_api.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Subject createSubject(SubjectRequest request) {
        Subject subject = new Subject();
        subject.setName(request.name());
        subject.setUserId(request.userId());
        return subjectRepository.save(subject);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
    }

    @Override
    public List<Subject> getSubjectsByUserId(Long userId) {
        return subjectRepository.findByUserId(userId);
    }

    @Override
    public Subject updateSubject(Long id, String name) {
        Subject subject = getSubjectById(id);
        subject.setName(name);
        return subjectRepository.save(subject);
    }

    @Override
    public Subject deleteSubject(Long id) {
        Subject subject = getSubjectById(id);
        subjectRepository.delete(subject);
        return subject;
    }
}
