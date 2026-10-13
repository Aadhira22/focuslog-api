# FocusLog API

A Spring Boot REST API for tracking how you study. It manages subjects, topics, practice question logs and study sessions, and calculates stats like accuracy per subject and study time per week.

## Features

- User registration and JWT login
- Subjects and topics
- Practice question logs (correct / wrong answers)
- Study session time tracking
- Stats: accuracy per subject, weekly study time
- Swagger API documentation

## Tech Stack

- Java 21, Spring Boot 3.5
- Spring Data JPA, H2
- JWT (jjwt), bcrypt
- Swagger / OpenAPI
- JUnit 5, Mockito

## Getting Started

You need JDK 21.

```bash
./mvnw spring-boot:run
```

- Swagger UI: http://localhost:8080/focuslog-api/swagger-ui.html
- H2 console: http://localhost:8080/focuslog-api/h2-console

Demo login: `demo@focuslog.dev` / `password123`

## Tests

```bash
./mvnw test
```

## Project Structure

```
src/main/java/com/example/focuslog_api/
├── config/       CORS, Swagger, password encoder
├── controller/   REST endpoints
├── dto/          request/response objects and mappers
├── exception/    custom exceptions and error handler
├── model/        JPA entities
├── repository/   Spring Data repositories
└── service/      business logic
```
