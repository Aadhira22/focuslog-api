package com.example.focuslog_api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.focuslog_api.dto.response.SubjectAccuracyResponse;
import com.example.focuslog_api.dto.response.WeeklyStudyTimeResponse;
import com.example.focuslog_api.exception.ResourceNotFoundException;
import com.example.focuslog_api.model.QuestionLog;
import com.example.focuslog_api.model.StudySession;
import com.example.focuslog_api.model.Subject;
import com.example.focuslog_api.repository.QuestionLogRepository;
import com.example.focuslog_api.repository.StudySessionRepository;
import com.example.focuslog_api.repository.SubjectRepository;
import com.example.focuslog_api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final long MINUTE_MS = 60_000L;

    @Mock private UserRepository userRepository;
    @Mock private SubjectRepository subjectRepository;
    @Mock private QuestionLogRepository questionLogRepository;
    @Mock private StudySessionRepository studySessionRepository;

    @InjectMocks private StatsServiceImpl statsService;

    // ---------- accuracy by subject ----------

    @Test
    void accuracyBySubject_sumsAllLogsOfEachSubjectAndRoundsToTwoDecimals() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(subjectRepository.findByUserId(USER_ID))
                .thenReturn(List.of(subject(10L, "Java"), subject(20L, "React")));
        when(questionLogRepository.findByUserId(USER_ID)).thenReturn(List.of(
                questionLog(10L, 20, 15),   // Java: 15 of 20
                questionLog(10L, 10, 5),    // Java: 5 of 10  -> 20 of 30 = 66.666...
                questionLog(20L, 10, 9)));  // React: 9 of 10 = 90

        List<SubjectAccuracyResponse> result = statsService.getAccuracyBySubject(USER_ID);

        assertThat(result)
                .extracting(
                        SubjectAccuracyResponse::subjectName,
                        SubjectAccuracyResponse::totalQuestions,
                        SubjectAccuracyResponse::correctAnswers,
                        SubjectAccuracyResponse::accuracyPercent)
                .containsExactly(
                        tuple("Java", 30L, 20L, 66.67),
                        tuple("React", 10L, 9L, 90.0));
    }

    @Test
    void accuracyBySubject_subjectWithNoLogs_hasNullAccuracyInsteadOfZero() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(subjectRepository.findByUserId(USER_ID)).thenReturn(List.of(subject(10L, "English")));
        when(questionLogRepository.findByUserId(USER_ID)).thenReturn(List.of());

        List<SubjectAccuracyResponse> result = statsService.getAccuracyBySubject(USER_ID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).totalQuestions()).isZero();
        assertThat(result.get(0).accuracyPercent()).isNull();
    }

    @Test
    void accuracyBySubject_allWrongAnswers_isZeroPercentNotNull() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(subjectRepository.findByUserId(USER_ID)).thenReturn(List.of(subject(10L, "Java")));
        when(questionLogRepository.findByUserId(USER_ID)).thenReturn(List.of(questionLog(10L, 10, 0)));

        List<SubjectAccuracyResponse> result = statsService.getAccuracyBySubject(USER_ID);

        assertThat(result.get(0).accuracyPercent()).isEqualTo(0.0);
    }

    @Test
    void accuracyBySubject_unknownUser_throwsNotFoundAndQueriesNothingElse() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> statsService.getAccuracyBySubject(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verifyNoMoreInteractions(subjectRepository, questionLogRepository);
    }

    // ---------- weekly study time ----------

    @Test
    void weeklyStudyTime_groupsByMondayToSundayWeekAndSortsOldestFirst() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(subjectRepository.findByUserId(USER_ID)).thenReturn(List.of(subject(10L, "Java")));
        // Deliberately out of order. 2026-10-05 and 2026-10-12 are Mondays.
        when(studySessionRepository.findBySubjectIdIn(List.of(10L))).thenReturn(List.of(
                studySession(10L, LocalDateTime.of(2026, 10, 12, 0, 0), 45 * MINUTE_MS),   // Monday, week 2
                studySession(10L, LocalDateTime.of(2026, 10, 11, 23, 59), 30 * MINUTE_MS), // Sunday, still week 1
                studySession(10L, LocalDateTime.of(2026, 10, 5, 8, 0), 30 * MINUTE_MS)));  // Monday, week 1

        List<WeeklyStudyTimeResponse> result = statsService.getWeeklyStudyTime(USER_ID);

        assertThat(result)
                .extracting(
                        WeeklyStudyTimeResponse::weekStart,
                        WeeklyStudyTimeResponse::weekEnd,
                        WeeklyStudyTimeResponse::totalMs,
                        WeeklyStudyTimeResponse::totalMinutes)
                .containsExactly(
                        tuple(LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 11), 60 * MINUTE_MS, 60.0),
                        tuple(LocalDate.of(2026, 10, 12), LocalDate.of(2026, 10, 18), 45 * MINUTE_MS, 45.0));
    }

    @Test
    void weeklyStudyTime_sumsSessionsFromDifferentSubjectsInTheSameWeek() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(subjectRepository.findByUserId(USER_ID))
                .thenReturn(List.of(subject(10L, "Java"), subject(20L, "React")));
        when(studySessionRepository.findBySubjectIdIn(List.of(10L, 20L))).thenReturn(List.of(
                studySession(10L, LocalDateTime.of(2026, 10, 6, 9, 0), 20 * MINUTE_MS),
                studySession(20L, LocalDateTime.of(2026, 10, 8, 9, 0), 40 * MINUTE_MS)));

        List<WeeklyStudyTimeResponse> result = statsService.getWeeklyStudyTime(USER_ID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).totalMs()).isEqualTo(60 * MINUTE_MS);
    }

    @Test
    void weeklyStudyTime_roundsMinutesToTwoDecimals() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(subjectRepository.findByUserId(USER_ID)).thenReturn(List.of(subject(10L, "Java")));
        when(studySessionRepository.findBySubjectIdIn(List.of(10L))).thenReturn(List.of(
                studySession(10L, LocalDateTime.of(2026, 10, 6, 9, 0), 100_000L))); // 1.666... minutes

        List<WeeklyStudyTimeResponse> result = statsService.getWeeklyStudyTime(USER_ID);

        assertThat(result.get(0).totalMinutes()).isEqualTo(1.67);
    }

    @Test
    void weeklyStudyTime_userWithNoSubjects_returnsEmptyListWithoutQueryingSessions() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(subjectRepository.findByUserId(USER_ID)).thenReturn(List.of());

        assertThat(statsService.getWeeklyStudyTime(USER_ID)).isEmpty();

        verifyNoMoreInteractions(studySessionRepository);
    }

    @Test
    void weeklyStudyTime_unknownUser_throwsNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> statsService.getWeeklyStudyTime(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ---------- test data helpers ----------

    private static Subject subject(Long id, String name) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        subject.setUserId(USER_ID);
        return subject;
    }

    private static QuestionLog questionLog(Long subjectId, long total, long correct) {
        QuestionLog log = new QuestionLog();
        log.setSubjectId(subjectId);
        log.setUserId(USER_ID);
        log.setTotalQuestions(total);
        log.setCorrectAnswers(correct);
        log.setWrongAnswers(total - correct);
        return log;
    }

    private static StudySession studySession(Long subjectId, LocalDateTime createdAt, long durationMs) {
        StudySession session = new StudySession();
        session.setSubjectId(subjectId);
        session.setCreatedAt(createdAt);
        session.setDurationMs(durationMs);
        return session;
    }
}
