package com.example.focuslog_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.focuslog_api.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByUserId(Long userId);
}
