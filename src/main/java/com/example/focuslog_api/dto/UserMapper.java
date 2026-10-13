package com.example.focuslog_api.dto;

import com.example.focuslog_api.dto.response.UserResponse;
import com.example.focuslog_api.model.User;

public class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
