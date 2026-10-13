package com.example.focuslog_api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.focuslog_api.dto.request.LoginRequest;
import com.example.focuslog_api.dto.response.LoginResponse;
import com.example.focuslog_api.exception.InvalidCredentialsException;
import com.example.focuslog_api.model.User;
import com.example.focuslog_api.repository.UserRepository;
import com.example.focuslog_api.service.JwtTokenService;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenService jwtTokenService;

    @InjectMocks private AuthServiceImpl authService;

    @Test
    void login_withCorrectPassword_returnsToken() {
        User user = user("ada@example.com", "stored-hash");
        when(userRepository.findByEmail("ada@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "stored-hash")).thenReturn(true);
        when(jwtTokenService.generateToken(user)).thenReturn("jwt-token");

        LoginResponse response = authService.login(new LoginRequest("ada@example.com", "secret"));

        assertThat(response.token()).isEqualTo("jwt-token");
    }

    @Test
    void login_withWrongPassword_throwsAndIssuesNoToken() {
        User user = user("ada@example.com", "stored-hash");
        when(userRepository.findByEmail("ada@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "stored-hash")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("ada@example.com", "wrong")))
                .isInstanceOf(InvalidCredentialsException.class);

        verifyNoMoreInteractions(jwtTokenService);
    }

    @Test
    void login_withUnknownEmail_throwsTheSameErrorAndNeverChecksAPassword() {
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequest("ghost@example.com", "secret")))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid email or password");

        verifyNoMoreInteractions(passwordEncoder, jwtTokenService);
    }

    private static User user(String email, String passwordHash) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        return user;
    }
}
