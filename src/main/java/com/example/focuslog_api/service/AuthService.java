package com.example.focuslog_api.service;

import com.example.focuslog_api.dto.request.LoginRequest;
import com.example.focuslog_api.dto.response.LoginResponse;

public interface AuthService {

    /** Validates the credentials and returns a signed JWT, or throws InvalidCredentialsException. */
    LoginResponse login(LoginRequest request);
}
