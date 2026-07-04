# Backlog & Sprint Plan — QA Test Case & Defect Tracker

## Product Vision
A lightweight Test Case & Defect Tracker that lets a QA engineer create test cases,
record execution results, log defects for failures, and view a quick pass/fail
health summary — mirroring a real QA workflow in a single small service.

## Product Backlog

| # | User Story | Priority | Estimate (pts) | Sprint |
|---|---|---|---|---|
| 1 | As a QA engineer, I want to create a test case (title, steps, expected result) so I can document what to verify | High | 3 | Sprint 1 |
| 2 | As a QA engineer, I want to list all test cases so I can see what needs execution | High | 2 | Sprint 1 |
| 3 | As a QA engineer, I want to update a test case's status (Pass/Fail/Blocked) after running it so I can track results | High | 3 | Sprint 1 |
| 4 | As a QA engineer, I want to log a defect linked to a failed test case so I can report the issue | Medium | 5 | Sprint 2 |
| 5 | As a QA engineer, I want to view a summary report (total/pass/fail counts) so I can assess quality at a glance | Medium | 3 | Sprint 2 |
| 6 | As a QA engineer, I want a health-check endpoint so I can confirm the service is running | Low | 1 | Sprint 2 |
| 7 | As a QA engineer, I want to delete/archive an obsolete test case so the backlog stays clean | Low | 2 | Backlog (stretch) |

## Definition of Done (DoD)
A story is "Done" when:
- Code is committed with a clear message referencing the story
- Unit test(s) exist and pass for the new behavior
- Endpoint manually verified (via Postman/curl) to match acceptance criteria
- CI pipeline runs green on the commit
- No known regressions in previously delivered stories

## Acceptance Criteria

**Story #1 — Create a test case**
- Given valid title/steps/expected result, POST /api/testcases returns 201 with the created test case, defaulting to status `NOT_RUN`.
- Given a missing required field, the API returns 400 with a clear error message.

**Story #2 — List all test cases**
- GET /api/testcases returns 200 and a JSON array of all created test cases.

**Story #3 — Update test case status**
- Given a valid test case ID, PUT /api/testcases/{id}/status updates and returns the test case with its new status.
- Given an invalid ID, the API returns 404.

**Story #4 — Log a defect (Sprint 2)**
- A defect can be created and linked to a specific test case ID.
- A defect includes a description and severity.

**Story #5 — Summary report (Sprint 2)**
- GET endpoint returns total test case count and counts per status (PASS/FAIL/BLOCKED/NOT_RUN).

**Story #6 — Health endpoint (Sprint 2)**
- GET /actuator/health returns 200 with status `UP` when the service is running.

## Sprint 1 Plan
**Selected stories:** #1, #2, #3 (8 points total)
**Sprint goal:** Deliver core test case CRUD + status tracking, with CI pipeline running tests on every push.

## Sprint 2 Plan
**Selected stories:** #4, #5, #6 (9 points total)
**Sprint goal:** Add defect logging, a quality summary report, and basic monitoring (health endpoint), incorporating Sprint 1 retrospective improvements.
