package com.example.focuslog_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focuslog_api.dto.response.SubjectAccuracyResponse;
import com.example.focuslog_api.dto.response.WeeklyStudyTimeResponse;
import com.example.focuslog_api.service.StatsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/stats/users/{userId}")
@Tag(name = "Stats", description = "Progress statistics calculated from a user's logged practice and study time")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/accuracy-by-subject")
    @Operation(
        summary = "Accuracy percentage per subject",
        description = "For each of the user's subjects: total questions, correct answers and accuracy = correct / total * 100 (2 decimals). Accuracy is null for subjects with no logged questions.")
    public List<SubjectAccuracyResponse> getAccuracyBySubject(@PathVariable Long userId) {
        return statsService.getAccuracyBySubject(userId);
    }

    @GetMapping("/weekly-study-time")
    @Operation(
        summary = "Total study time per week",
        description = "Study time summed per ISO week (Monday to Sunday) across all of the user's subjects, oldest week first. Weeks with no study time are omitted.")
    public List<WeeklyStudyTimeResponse> getWeeklyStudyTime(@PathVariable Long userId) {
        return statsService.getWeeklyStudyTime(userId);
    }
}
