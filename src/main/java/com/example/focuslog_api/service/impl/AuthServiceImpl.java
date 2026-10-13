package com.example.focuslog_api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.focuslog_api.dto.request.LoginRequest;
import com.example.focuslog_api.dto.response.LoginResponse;
import com.example.focuslog_api.exception.InvalidCredentialsException;
import com.example.focuslog_api.model.User;
import com.example.focuslog_api.repository.UserRepository;
import com.example.focuslog_api.service.AuthService;
import com.example.focuslog_api.service.JwtTokenService;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String INVALID_CREDENTIALS = "Invalid email or password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // Same error for "unknown email" and "wrong password" so callers can't probe which emails exist.
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }

        return new LoginResponse("Login successful", jwtTokenService.generateToken(user));
    }
}
