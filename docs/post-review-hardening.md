# Post-Review Hardening

The following issues were identified in an external code review after Sprint 2 and were
addressed before final submission. This is **not** a "Sprint 3" — the assessment brief specifies
exactly two sprints. It's a transparent, post-submission hardening pass driven by external code
review, done on a separate `hardening-pass` branch as new, additive commits on top of the
Sprint 1/2 history. Responding to code review with atomic, tested commits is itself a real
Agile/DevOps practice, which is why it's documented here rather than folded silently into the
Sprint 2 record.

## What was found and what closed it

| # | Issue found in review | What was done about it | Commit(s) |
|---|---|---|---|
| 1 | **Validation inconsistency:** `TestCaseController.createTestCase` was missing `@Valid`, so `CreateTestCaseRequest`'s `@NotBlank` annotations were dead code; it silently fell back to manual checks and returned a plain-text error, unlike `DefectController` which already used `@Valid` + field-level JSON errors. This contradicted the Sprint 2 review's claim that manual checks had been replaced everywhere. | Added `@Valid` to `TestCaseController.createTestCase`. Kept the manual guard clauses in `TestCaseService` as a documented defense-in-depth backstop for callers that bypass the web layer. Added `WebMvcTest` coverage for both controllers. Corrected `sprint2-review.md` and updated `manual-test-script.md` step 11 to show the real (JSON) response shape. | `86b256e` |
| 2 | **In-memory-only persistence:** `TestCaseRepository`/`DefectRepository` were `ConcurrentHashMap`s — all data lost on restart, flagged by both Sprint 1 stakeholder feedback and the Sprint 2 retro. | Added `spring-boot-starter-data-jpa` + H2, converted `TestCase`/`Defect` to JPA entities, replaced the repositories with `JpaRepository` interfaces, and pointed `application.properties` at a file-based H2 database (`jdbc:h2:file:./data/qatracker`). Unit tests now run against a real (embedded, in-memory) JPA repository via `@DataJpaTest`, isolated from the runtime database. See `docs/persistence-note.md` for a restart evidence transcript. | `354ede3` |
| 3 | **Missing defect lifecycle:** Sprint 2 retro listed this as a known gap — a defect, once logged, had no way to be marked resolved. | Added a `DefectStatus` enum (`OPEN`/`RESOLVED`, defaulting to `OPEN`) and `PUT /api/defects/{id}/status` (404 if not found), mirroring the existing test case status-update pattern. Added unit + controller tests for both the happy path and not-found case. | `d754a89` |
| 4 | **Missing severity breakdown:** Sprint 2 retro also listed this — the summary report only covered test case statuses, not defect severity. | Added `GET /api/defects/summary`, returning defect counts grouped by severity and by the new status field, as a separate endpoint from `/api/testcases/summary` (a different resource's report, kept in its own controller rather than merged into `SummaryReportResponse`). Added unit + controller tests. | `d754a89` |
| 5 | **CI-only pipeline, no CD step:** the pipeline built the jar and ran tests, but did nothing that resembled delivery. | Added a multi-stage `Dockerfile` (Maven build stage → slim JRE runtime stage) and a `docker-smoke-test` job that runs after `build-and-test`: builds the image, runs it as a container, polls `/actuator/health` from the CI runner, asserts `UP`, then tears the container down — a real smoke test, not just a build. Added a CI status badge to `README.md`. | `9aa3690` |

## Evidence

The app was built and run locally (`mvn clean package -DskipTests` then
`java -jar target/qa-test-tracker-0.0.1-SNAPSHOT.jar`) to capture real request/response output for
every new or changed endpoint below — nothing here is a fabricated example.

### Issue #1 — validation now uniform (`TestCaseController`)

```
POST /api/testcases
{ "title": "", "steps": "some steps", "expectedResult": "some result" }

HTTP/1.1 400
{"title":"Title is required"}
```

Before this fix, the same request returned a plain-text `400` body (e.g. `"Title is required"`
with no JSON structure) instead of a field-level error object.

### Issue #3 — defect lifecycle (`PUT /api/defects/{id}/status`)

Setup — created a test case, marked it `FAIL`, and logged a defect against it:

```
POST /api/testcases
{ "title": "Login with valid credentials", "steps": "1. Open login 2. Enter creds 3. Submit", "expectedResult": "User logged in" }
-> 201
{"id":1,"title":"Login with valid credentials","steps":"1. Open login 2. Enter creds 3. Submit","expectedResult":"User logged in","status":"NOT_RUN"}

PUT /api/testcases/1/status
{ "status": "FAIL" }
-> 200
{"id":1,"title":"Login with valid credentials","steps":"1. Open login 2. Enter creds 3. Submit","expectedResult":"User logged in","status":"FAIL"}

POST /api/defects
{ "testCaseId": 1, "description": "Login button unresponsive on submit", "severity": "HIGH" }
-> 201
{"id":1,"testCaseId":1,"description":"Login button unresponsive on submit","severity":"HIGH","status":"OPEN"}
```

Happy path — resolve the defect:

```
PUT /api/defects/1/status
{ "status": "RESOLVED" }

HTTP/1.1 200
{"id":1,"testCaseId":1,"description":"Login button unresponsive on submit","severity":"HIGH","status":"RESOLVED"}
```

Not-found path:

```
PUT /api/defects/999/status
{ "status": "RESOLVED" }

HTTP/1.1 404
Defect 999 not found
```

### Issue #4 — severity/status breakdown (`GET /api/defects/summary`)

With the one `HIGH`/`RESOLVED` defect from above:

```
GET /api/defects/summary

HTTP/1.1 200
{"total":1,"bySeverity":{"LOW":0,"MEDIUM":0,"HIGH":1,"CRITICAL":0},"byStatus":{"OPEN":0,"RESOLVED":1}}
```

### Issue #2 — persistence

See `docs/persistence-note.md` for the full curl-before/curl-after-restart transcript.

### Issue #5 — CD smoke test

Not reproducible as a local curl transcript (this environment has no Docker installed) — the
`docker-smoke-test` job in `.github/workflows/ci.yml` performs the equivalent check on every
push/PR: it builds the image, runs the container, and curls `/actuator/health` from the runner,
failing the job (and dumping container logs) if `status` isn't `UP`.
