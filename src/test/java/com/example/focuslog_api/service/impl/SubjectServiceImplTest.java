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

import com.example.focuslog_api.dto.request.SubjectRequest;
import com.example.focuslog_api.exception.ResourceNotFoundException;
import com.example.focuslog_api.model.Subject;
import com.example.focuslog_api.repository.SubjectRepository;

@ExtendWith(MockitoExtension.class)
class SubjectServiceImplTest {

    @Mock private SubjectRepository subjectRepository;

    @InjectMocks private SubjectServiceImpl subjectService;

    @Test
    void createSubject_savesNameAndOwner() {
        when(subjectRepository.save(any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subject saved = subjectService.createSubject(new SubjectRequest("Java", 7L));

        assertThat(saved.getName()).isEqualTo("Java");
        assertThat(saved.getUserId()).isEqualTo(7L);
    }

    @Test
    void getSubjectById_whenMissing_throwsNotFound() {
        when(subjectRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subjectService.getSubjectById(5L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("5");
    }

    @Test
    void updateSubject_renamesAndSaves() {
        Subject existing = subject(5L, "Old name");
        when(subjectRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(subjectRepository.save(existing)).thenReturn(existing);

        Subject updated = subjectService.updateSubject(5L, "New name");

        assertThat(updated.getName()).isEqualTo("New name");
    }

    @Test
    void updateSubject_whenMissing_throwsNotFoundAndSavesNothing() {
        when(subjectRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subjectService.updateSubject(5L, "New name"))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    void deleteSubject_deletesAndReturnsTheRemovedSubject() {
        Subject existing = subject(5L, "Java");
        when(subjectRepository.findById(5L)).thenReturn(Optional.of(existing));

        Subject deleted = subjectService.deleteSubject(5L);

        assertThat(deleted).isSameAs(existing);
        verify(subjectRepository).delete(existing);
    }

    private static Subject subject(Long id, String name) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        return subject;
    }
}
