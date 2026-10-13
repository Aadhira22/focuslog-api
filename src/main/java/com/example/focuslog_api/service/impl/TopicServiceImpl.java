package com.example.focuslog_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.focuslog_api.dto.request.TopicRequest;
import com.example.focuslog_api.exception.ResourceNotFoundException;
import com.example.focuslog_api.model.Topic;
import com.example.focuslog_api.repository.TopicRepository;
import com.example.focuslog_api.service.TopicService;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public Topic createTopic(TopicRequest request) {
        Topic topic = new Topic();
        topic.setName(request.name());
        topic.setSubjectId(request.subjectId());
        return topicRepository.save(topic);
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @Override
    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
    }

    @Override
    public List<Topic> getTopicsBySubjectId(Long subjectId) {
        return topicRepository.findBySubjectId(subjectId);
    }

    @Override
    public Topic updateTopic(Long id, String name) {
        Topic topic = getTopicById(id);
        topic.setName(name);
        return topicRepository.save(topic);
    }

    @Override
    public Topic deleteTopic(Long id) {
        Topic topic = getTopicById(id);
        topicRepository.delete(topic);
        return topic;
    }
}
