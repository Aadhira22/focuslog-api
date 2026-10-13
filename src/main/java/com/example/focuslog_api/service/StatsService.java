package com.example.focuslog_api.service;

import java.util.List;

import com.example.focuslog_api.dto.response.SubjectAccuracyResponse;
import com.example.focuslog_api.dto.response.WeeklyStudyTimeResponse;

public interface StatsService {

    /** Accuracy (% correct) for each of the user's subjects. */
    List<SubjectAccuracyResponse> getAccuracyBySubject(Long userId);

    /** Total study time per ISO week across all of the user's subjects, oldest week first. */
    List<WeeklyStudyTimeResponse> getWeeklyStudyTime(Long userId);
}
