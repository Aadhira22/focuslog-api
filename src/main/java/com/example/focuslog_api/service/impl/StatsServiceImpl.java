package com.example.focuslog_api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
import com.example.focuslog_api.service.StatsService;

@Service
public class StatsServiceImpl implements StatsService {

    private static final long MS_PER_MINUTE = 60_000L;

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final QuestionLogRepository questionLogRepository;
    private final StudySessionRepository studySessionRepository;

    public StatsServiceImpl(UserRepository userRepository,
                            SubjectRepository subjectRepository,
                            QuestionLogRepository questionLogRepository,
                            StudySessionRepository studySessionRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.questionLogRepository = questionLogRepository;
        this.studySessionRepository = studySessionRepository;
    }

    @Override
    public List<SubjectAccuracyResponse> getAccuracyBySubject(Long userId) {
        requireUser(userId);

        List<Subject> subjects = subjectRepository.findByUserId(userId);

        // One query for all of the user's logs, grouped in memory, instead of one query per subject.
        Map<Long, List<QuestionLog>> logsBySubject = questionLogRepository.findByUserId(userId).stream()
                .collect(Collectors.groupingBy(QuestionLog::getSubjectId));

        return subjects.stream()
                .map(subject -> toAccuracy(subject, logsBySubject.getOrDefault(subject.getId(), List.of())))
                .toList();
    }

    @Override
    public List<WeeklyStudyTimeResponse> getWeeklyStudyTime(Long userId) {
        requireUser(userId);

        // Study sessions have no user id of their own; they belong to a user through their subject.
        List<Long> subjectIds = subjectRepository.findByUserId(userId).stream()
                .map(Subject::getId)
                .toList();
        if (subjectIds.isEmpty()) {
            return List.of();
        }

        // TreeMap keeps the weeks sorted oldest to newest, keyed by the Monday that starts each week.
        Map<LocalDate, Long> totalMsByWeekStart = new TreeMap<>();
        for (StudySession session : studySessionRepository.findBySubjectIdIn(subjectIds)) {
            LocalDate weekStart = session.getCreatedAt().toLocalDate()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            totalMsByWeekStart.merge(weekStart, session.getDurationMs(), Long::sum);
        }

        return totalMsByWeekStart.entrySet().stream()
                .map(entry -> new WeeklyStudyTimeResponse(
                        entry.getKey(),
                        entry.getKey().plusDays(6),
                        entry.getValue(),
                        toMinutes(entry.getValue())))
                .toList();
    }

    private SubjectAccuracyResponse toAccuracy(Subject subject, List<QuestionLog> logs) {
        long total = logs.stream().mapToLong(QuestionLog::getTotalQuestions).sum();
        long correct = logs.stream().mapToLong(QuestionLog::getCorrectAnswers).sum();
        return new SubjectAccuracyResponse(subject.getId(), subject.getName(), total, correct,
                accuracyPercent(correct, total));
    }

    /** Percent correct to 2 decimals, or null if there is nothing to measure yet. */
    private Double accuracyPercent(long correct, long total) {
        if (total == 0) {
            return null;
        }
        return BigDecimal.valueOf(correct)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private double toMinutes(long ms) {
        return BigDecimal.valueOf(ms)
                .divide(BigDecimal.valueOf(MS_PER_MINUTE), 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private void requireUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }
}
