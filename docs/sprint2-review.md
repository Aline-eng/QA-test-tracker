# Sprint 2 Review

## Sprint Goal
Add defect logging, a quality summary report, and basic monitoring (health endpoint), incorporating Sprint 1 retrospective improvements.

## Retrospective Improvements Applied
| Improvement | Applied As |
|---|---|
| Structured logging | Added SLF4J logging in `TestCaseService` and `DefectService` for creation/status-change/defect events |
| Fixed manual test script | See `docs/manual-test-script.md` — run in order for every demo, ensures consistent verification |
| Input validation framework | Replaced manual null/blank checks with `spring-boot-starter-validation` (`@NotBlank`, `@NotNull`) and a `GlobalExceptionHandler` returning field-level 400 errors |

## Delivered Stories
| Story | Status | Evidence |
|---|---|---|
| #4 — Log a defect linked to a test case | ✅ Done | See demo below |
| #5 — Summary report | ✅ Done | See demo below |
| #6 — Health endpoint | ✅ Done | See demo below |

## Demo
Follow `docs/manual-test-script.md` steps 6–10 for this sprint's new functionality:

### Story #4: Log a defect
![Log a defect against the failed test case.jpg](../Screenshots/Log%20a%20defect%20against%20the%20failed%20test%20case.jpg)[SCREENSHOT: Postman POST /api/defects request + 201 response]
![negative test(without a title).jpg](../Screenshots/negative%20test%28without%20a%20title%29.jpg)

### Story #5: Summary report
![get summary report.jpg](../Screenshots/get%20summary%20report.jpg)

### Story #6: Health endpoint
![Check health endpoint.jpg](../Screenshots/Check%20health%20endpoint.jpg)

## CI/CD Evidence
[SCREENSHOT: GitHub Actions run for the Sprint 2 commits, green checkmark]

## Testing Evidence
[SCREENSHOT: IntelliJ or `mvn test` output showing all tests passing, including new DefectServiceTest and summary report tests]

## Commit History Evidence
[SCREENSHOT: GitHub commits page showing Sprint 2's incremental commits]

## Stakeholder Feedback
- Positive: defect logging closes the loop from "test failed" to "here's why" — matches real QA workflows.
- Positive: the summary report gives an at-a-glance health check without needing to inspect every test case.
- Concern: defects have no status of their own (open/resolved) — noted as a future backlog item beyond this assessment's scope.
