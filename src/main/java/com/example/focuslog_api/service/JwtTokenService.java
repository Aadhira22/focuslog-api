package com.example.focuslog_api.service;

import com.example.focuslog_api.model.User;

public interface JwtTokenService {
    
    String generateToken(User user);

    boolean validateToken(String token);

    String getUserFromToken(String token);
}
