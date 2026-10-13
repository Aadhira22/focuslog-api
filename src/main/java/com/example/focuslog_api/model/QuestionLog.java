package com.example.focuslog_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * One practice session: how many questions the user answered on a topic and how many were right/wrong.
 * Formerly {@code Questao}.
 */
@Entity
@Table(name = "question_logs")
public class QuestionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_questions", nullable = false)
    private Long totalQuestions;

    @Column(name = "correct_answers", nullable = false)
    private Long correctAnswers;

    @Column(name = "wrong_answers", nullable = false)
    private Long wrongAnswers;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Long totalQuestions) { this.totalQuestions = totalQuestions; }

    public Long getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(Long correctAnswers) { this.correctAnswers = correctAnswers; }

    public Long getWrongAnswers() { return wrongAnswers; }
    public void setWrongAnswers(Long wrongAnswers) { this.wrongAnswers = wrongAnswers; }

    public Long getTopicId() { return topicId; }
    public void setTopicId(Long topicId) { this.topicId = topicId; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
