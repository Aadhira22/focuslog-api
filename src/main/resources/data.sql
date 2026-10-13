-- Demo data, loaded on every startup (in-memory H2).
-- Every seeded user logs in with the password: password123
-- IDs below rely on the auto-increment starting at 1 on a fresh database.
-- Dates are relative to "now" so the weekly stats always have recent data.

-- Users
INSERT INTO users (name, email, password_hash) VALUES ('Demo Student', 'demo@focuslog.dev', '$2a$10$EwdHQ8jBD3SNXsKXUAXxIe2h2SHRYv2eo8Sz97CT28vtUsLt1WNP6');
INSERT INTO users (name, email, password_hash) VALUES ('Second Student', 'student2@focuslog.dev', '$2a$10$EwdHQ8jBD3SNXsKXUAXxIe2h2SHRYv2eo8Sz97CT28vtUsLt1WNP6');

-- Subjects (user 1 has three, user 2 has one)
INSERT INTO subjects (name, user_id) VALUES ('Java and Spring Boot', 1);
INSERT INTO subjects (name, user_id) VALUES ('React and Ecosystem', 1);
INSERT INTO subjects (name, user_id) VALUES ('Professional English', 1);
INSERT INTO subjects (name, user_id) VALUES ('Algorithms', 2);

-- Topics
INSERT INTO topics (name, subject_id) VALUES ('JPA and Hibernate mapping', 1);
INSERT INTO topics (name, subject_id) VALUES ('REST API design', 1);
INSERT INTO topics (name, subject_id) VALUES ('State management and hooks', 2);
INSERT INTO topics (name, subject_id) VALUES ('Job interview vocabulary', 3);
INSERT INTO topics (name, subject_id) VALUES ('Graph algorithms', 4);

-- Question logs: total, correct, wrong, topic, subject, user, created_at
-- Java: 45 questions, 36 correct  -> 80.0%
-- React: 30 questions, 20 correct -> 66.67%
-- English: no questions yet       -> accuracy null
INSERT INTO question_logs (total_questions, correct_answers, wrong_answers, topic_id, subject_id, user_id, created_at)
VALUES (20, 15, 5, 1, 1, 1, DATEADD(DAY, -15, CURRENT_TIMESTAMP));
INSERT INTO question_logs (total_questions, correct_answers, wrong_answers, topic_id, subject_id, user_id, created_at)
VALUES (15, 12, 3, 2, 1, 1, DATEADD(DAY, -8, CURRENT_TIMESTAMP));
INSERT INTO question_logs (total_questions, correct_answers, wrong_answers, topic_id, subject_id, user_id, created_at)
VALUES (10, 9, 1, 1, 1, 1, DATEADD(DAY, -2, CURRENT_TIMESTAMP));
INSERT INTO question_logs (total_questions, correct_answers, wrong_answers, topic_id, subject_id, user_id, created_at)
VALUES (15, 9, 6, 3, 2, 1, DATEADD(DAY, -9, CURRENT_TIMESTAMP));
INSERT INTO question_logs (total_questions, correct_answers, wrong_answers, topic_id, subject_id, user_id, created_at)
VALUES (15, 11, 4, 3, 2, 1, DATEADD(DAY, -3, CURRENT_TIMESTAMP));
INSERT INTO question_logs (total_questions, correct_answers, wrong_answers, topic_id, subject_id, user_id, created_at)
VALUES (10, 6, 4, 5, 4, 2, DATEADD(DAY, -1, CURRENT_TIMESTAMP));

-- Study sessions: duration in milliseconds, topic, subject, created_at
-- (5400000 = 90 min, 3600000 = 60 min, 2700000 = 45 min, 1800000 = 30 min)
INSERT INTO study_sessions (duration_ms, topic_id, subject_id, created_at)
VALUES (5400000, 1, 1, DATEADD(DAY, -15, CURRENT_TIMESTAMP));
INSERT INTO study_sessions (duration_ms, topic_id, subject_id, created_at)
VALUES (3600000, 2, 1, DATEADD(DAY, -9, CURRENT_TIMESTAMP));
INSERT INTO study_sessions (duration_ms, topic_id, subject_id, created_at)
VALUES (2700000, 1, 1, DATEADD(DAY, -8, CURRENT_TIMESTAMP));
INSERT INTO study_sessions (duration_ms, topic_id, subject_id, created_at)
VALUES (3600000, 1, 1, DATEADD(DAY, -2, CURRENT_TIMESTAMP));
INSERT INTO study_sessions (duration_ms, topic_id, subject_id, created_at)
VALUES (3600000, 3, 2, DATEADD(DAY, -10, CURRENT_TIMESTAMP));
INSERT INTO study_sessions (duration_ms, topic_id, subject_id, created_at)
VALUES (1800000, 3, 2, DATEADD(DAY, -3, CURRENT_TIMESTAMP));
INSERT INTO study_sessions (duration_ms, topic_id, subject_id, created_at)
VALUES (2700000, 4, 3, DATEADD(DAY, -1, CURRENT_TIMESTAMP));
INSERT INTO study_sessions (duration_ms, topic_id, subject_id, created_at)
VALUES (3600000, 5, 4, DATEADD(DAY, -1, CURRENT_TIMESTAMP));
