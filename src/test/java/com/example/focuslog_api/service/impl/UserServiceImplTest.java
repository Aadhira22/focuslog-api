package com.example.focuslog_api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.focuslog_api.dto.request.UserRequest;
import com.example.focuslog_api.exception.ConflictException;
import com.example.focuslog_api.exception.ResourceNotFoundException;
import com.example.focuslog_api.model.User;
import com.example.focuslog_api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserServiceImpl userService;

    @Test
    void createUser_storesAHashNeverTheRawPassword() {
        when(userRepository.existsByEmail("ada@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("hashed-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.createUser(new UserRequest("Ada", "ada@example.com", "secret"));

        assertThat(saved.getName()).isEqualTo("Ada");
        assertThat(saved.getEmail()).isEqualTo("ada@example.com");
        assertThat(saved.getPasswordHash()).isEqualTo("hashed-secret");
    }

    @Test
    void createUser_withExistingEmail_throwsConflictAndSavesNothing() {
        when(userRepository.existsByEmail("ada@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(new UserRequest("Ada", "ada@example.com", "secret")))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("ada@example.com");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_whenMissing_throwsNotFound() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(42L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("42");
    }
}
