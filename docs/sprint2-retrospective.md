# Sprint 2 Retrospective

## What Went Well
- All three Sprint 1 retro improvements were successfully applied before starting new feature work, rather than being deferred indefinitely.
- Reusing `TestCaseService.getTestCaseById()` inside `DefectService` (rather than duplicating lookup logic) kept the defect-linking validation consistent with the rest of the app.
- The fixed manual test script made re-verifying the whole app after each change fast and repeatable, instead of re-inventing test steps each time.
- Validation errors are now structured and field-specific, which is a noticeably better developer experience than the Sprint 1 string-based error messages.

## What Didn't Go Well
- Defects still don't have their own lifecycle (e.g. Open/Resolved) — right now a defect, once logged, has no way to be marked as fixed.
- The summary report only covers test case statuses; it doesn't yet break down defects by severity, which would be useful for prioritization.
- Logging is basic (info-level only) — there's no distinction between expected business events and actual errors/exceptions being logged with more detail (e.g. stack traces).

## Lessons Learned
- Applying retrospective improvements *before* new features (rather than mixed in) made it easy to verify each improvement independently and kept commit history readable.
- Designing the health/monitoring story (#6) around Spring Boot Actuator, instead of hand-rolling a custom endpoint, saved time and is a more realistic "DevOps practice" than a bespoke solution.
- Writing the negative-path test (defect against a non-existent test case) early caught a real edge case in linking logic before it reached the demo stage.

## Possible Future Improvements (beyond this assessment's scope)
1. Add a defect status field (Open/Resolved) and an endpoint to update it.
2. Extend the summary report to include defect counts grouped by severity.
3. Move from in-memory storage to a persistent database (e.g. H2 or PostgreSQL) so data survives restarts.
