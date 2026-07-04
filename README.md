# QA Test Case & Defect Tracker

## Product Vision
A lightweight Test Case & Defect Tracker that lets a QA engineer create test cases,
record execution results, log defects for failures, and view a quick pass/fail
health summary — mirroring a real QA workflow in a single small service.

## Stack
Java 21, Spring Boot 3, Maven, JUnit 5

## Running locally
```
mvn spring-boot:run
```
Service runs on http://localhost:8080

## API Endpoints (Sprint 1)
| Method | Endpoint | Story | Description |
|--------|----------|-------|-------------|
| POST | /api/testcases | #1 | Create a test case |
| GET | /api/testcases | #2 | List all test cases |
| GET | /api/testcases/{id} | - | Get a single test case |
| PUT | /api/testcases/{id}/status | #3 | Update status (NOT_RUN/PASS/FAIL/BLOCKED) |

## Running tests
```
mvn test
```

## Backlog, Sprint Plans, Reviews & Retrospectives
See `/docs` folder (added as the project progresses).
