package com.example.focuslog_api.service;

import java.util.List;

import com.example.focuslog_api.dto.request.UserRequest;
import com.example.focuslog_api.model.User;

public interface UserService {

    User createUser(UserRequest request);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByEmail(String email);
}
