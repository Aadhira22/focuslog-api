package com.example.focuslog_api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.focuslog_api.dto.request.QuestionLogRequest;
import com.example.focuslog_api.exception.BadRequestException;
import com.example.focuslog_api.model.QuestionLog;
import com.example.focuslog_api.repository.QuestionLogRepository;

@ExtendWith(MockitoExtension.class)
class QuestionLogServiceImplTest {

    @Mock private QuestionLogRepository questionLogRepository;

    @InjectMocks private QuestionLogServiceImpl questionLogService;

    @Test
    void createQuestionLog_withConsistentCounts_savesThem() {
        when(questionLogRepository.save(any(QuestionLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        QuestionLog saved = questionLogService.createQuestionLog(request(10, 8, 2));

        assertThat(saved.getTotalQuestions()).isEqualTo(10L);
        assertThat(saved.getCorrectAnswers()).isEqualTo(8L);
        assertThat(saved.getWrongAnswers()).isEqualTo(2L);
    }

    @Test
    void createQuestionLog_allowsSkippedQuestions() {
        when(questionLogRepository.save(any(QuestionLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 10 questions, only 7 answered: correct + wrong < total is fine
        QuestionLog saved = questionLogService.createQuestionLog(request(10, 5, 2));

        assertThat(saved.getTotalQuestions()).isEqualTo(10L);
    }

    @Test
    void createQuestionLog_whenAnswersExceedTotal_throwsBadRequestAndSavesNothing() {
        // 8 + 5 = 13 answers for only 10 questions would give an accuracy above 100%
        assertThatThrownBy(() -> questionLogService.createQuestionLog(request(10, 8, 5)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("cannot exceed totalQuestions");

        verify(questionLogRepository, never()).save(any(QuestionLog.class));
    }

    private static QuestionLogRequest request(long total, long correct, long wrong) {
        return new QuestionLogRequest(total, correct, wrong, 1L, 1L, 1L);
    }
}
