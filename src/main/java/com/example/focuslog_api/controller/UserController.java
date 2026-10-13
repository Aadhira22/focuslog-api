package com.example.focuslog_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.focuslog_api.dto.UserMapper;
import com.example.focuslog_api.dto.request.UserRequest;
import com.example.focuslog_api.dto.response.UserResponse;
import com.example.focuslog_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a user", description = "The password is stored as a bcrypt hash. Returns 409 if the email is already taken.")
    public UserResponse createUser(@Valid @RequestBody UserRequest request) {
        return UserMapper.toResponse(userService.createUser(request));
    }

    @GetMapping
    @Operation(summary = "List all users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream().map(UserMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    public UserResponse getUserById(@PathVariable Long id) {
        return UserMapper.toResponse(userService.getUserById(id));
    }

    @GetMapping("/by-email/{email}")
    @Operation(summary = "Get a user by email")
    public UserResponse getUserByEmail(@PathVariable String email) {
        return UserMapper.toResponse(userService.getUserByEmail(email));
    }
}
