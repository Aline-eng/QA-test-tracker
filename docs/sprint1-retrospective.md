# Sprint 1 Retrospective

## What Went Well
- All 3 committed stories were delivered within scope — no scope creep.
- Test coverage was written alongside the service logic, not bolted on afterward, which made refactoring the controller layer safer.
- CI pipeline was set up early (not left until the end), so every subsequent commit was validated automatically.
- Commit history was planned around stories from the start, making progress traceable rather than one large "final" commit.

## What Didn't Go Well
- Data is stored in-memory only — every restart wipes all test cases. This is fine for a prototype/demo, but it's a real limitation to be transparent about.
- Error handling is done manually (`if` checks + try/catch) rather than using a structured validation framework, which won't scale well if more fields are added.
- No logging was in place yet — if something failed silently, there'd be no trace to debug from. This directly affects Sprint 2's monitoring requirement.
- Manual endpoint testing (RestPilot/Postman) was done rather than following a written test script, risking inconsistent verification between runs.

## Improvements for Sprint 2
1. **Add structured logging** (e.g. via SLF4J, already bundled with Spring Boot) at key points — test case creation, status updates, and defect logging — so behavior is traceable and this doubles as the "monitoring/logging" requirement for Sprint 2.
2. **Write a fixed manual test script** (a short checklist of requests to run in Postman in order) before demoing, so verification is consistent and repeatable rather than improvised each time.
3. **Introduce basic input validation via `spring-boot-starter-validation`** annotations (`@NotBlank`, etc.) instead of manual null/blank checks, since Sprint 2 adds a new entity (Defect) that will need the same validation logic.

## Action Owner
All actions owned by the developer (individual assessment) — to be applied at the start of Sprint 2.
