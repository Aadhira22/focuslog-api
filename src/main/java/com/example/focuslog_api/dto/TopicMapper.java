package com.example.focuslog_api.dto;

import com.example.focuslog_api.dto.response.TopicResponse;
import com.example.focuslog_api.model.Topic;

public class TopicMapper {

    private TopicMapper() {}

    public static TopicResponse toResponse(Topic topic) {
        return new TopicResponse(topic.getId(), topic.getName(), topic.getSubjectId());
    }
}
