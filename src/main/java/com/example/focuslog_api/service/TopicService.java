package com.example.focuslog_api.service;

import java.util.List;

import com.example.focuslog_api.dto.request.TopicRequest;
import com.example.focuslog_api.model.Topic;

public interface TopicService {

    Topic createTopic(TopicRequest request);

    List<Topic> getAllTopics();

    Topic getTopicById(Long id);

    List<Topic> getTopicsBySubjectId(Long subjectId);

    Topic updateTopic(Long id, String name);

    Topic deleteTopic(Long id);
}
