# QA Test Case & Defect Tracker

[![CI](https://github.com/Aline-eng/QA-test-tracker/actions/workflows/ci.yml/badge.svg)](https://github.com/Aline-eng/QA-test-tracker/actions/workflows/ci.yml)

## Product Vision
A lightweight Test Case & Defect Tracker that lets a QA engineer create test cases,
record execution results, log defects for failures, and view a quick pass/fail
health summary — mirroring a real QA workflow in a single small service.

## Stack
Java 21, Spring Boot 3, Spring Data JPA, H2 (file-based), Maven, JUnit 5

## Running locally
```
mvn spring-boot:run
```
Service runs on http://localhost:8080. Data is persisted to a file-based H2 database under
`./data/` (survives restarts) — see `docs/persistence-note.md`. The H2 console is available at
`/h2-console` (JDBC URL `jdbc:h2:file:./data/qatracker`, user `sa`, no password).

### Running with Docker
```
docker build -t qa-test-tracker .
docker run -p 8080:8080 qa-test-tracker
```

## API Endpoints
| Method | Endpoint | Story | Description |
|--------|----------|-------|-------------|
| POST | /api/testcases | #1 | Create a test case |
| GET | /api/testcases | #2 | List all test cases |
| GET | /api/testcases/{id} | - | Get a single test case |
| PUT | /api/testcases/{id}/status | #3 | Update status (NOT_RUN/PASS/FAIL/BLOCKED) |
| POST | /api/defects | #4 | Log a defect linked to a test case |
| GET | /api/defects | - | List all defects |
| PUT | /api/defects/{id}/status | Post-review hardening | Update defect status (OPEN/RESOLVED) — see `docs/post-review-hardening.md` |
| GET | /api/testcases/summary | #5 | Summary report: test case counts by status |
| GET | /api/defects/summary | Post-review hardening | Defect counts by severity and status — see `docs/post-review-hardening.md` |
| GET | /actuator/health | #6 | Health check |

## Running tests
```
mvn test
```

## Backlog, Sprint Plans, Reviews & Retrospectives
See `/docs` folder (added as the project progresses). Note: `docs/post-review-hardening.md`
documents a post-submission hardening pass driven by external code review — not a "Sprint 3".
