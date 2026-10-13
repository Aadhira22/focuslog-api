package com.example.focuslog_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.focuslog_api.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findBySubjectId(Long subjectId);
}
