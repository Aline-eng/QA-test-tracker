# Persistence Note (Post-Review Hardening)

## What changed

Both Sprint 1 stakeholder feedback (`sprint1-review.md`) and the Sprint 2
retrospective (`sprint2-retrospective.md`) flagged that all data lived in an
in-memory `ConcurrentHashMap` and was lost on every restart.

This has been fixed:

- Added `spring-boot-starter-data-jpa` and `com.h2database:h2` to `pom.xml`.
- `TestCase` and `Defect` (in `model/`) are now JPA entities (`@Entity`,
  `@Id @GeneratedValue`), with the same fields/getters/setters as before.
- `TestCaseRepository` and `DefectRepository` are now Spring Data JPA
  interfaces (`extends JpaRepository<T, Long>`), replacing the old
  hand-rolled `ConcurrentHashMap` implementations.
- `application.properties` points at a **file-based** H2 database
  (`jdbc:h2:file:./data/qatracker`), not an in-memory one, so the data
  directory on disk survives an app restart. `spring.jpa.hibernate.ddl-auto=update`
  keeps the schema in sync with the entities.
- The H2 console is enabled at `/h2-console` for demo purposes.
- Unit tests (`TestCaseServiceTest`, `DefectServiceTest`) now run against a
  real JPA repository backed by `@DataJpaTest`, which auto-configures a
  separate **embedded, in-memory** H2 database per test class and rolls
  back after each test — so the test suite never touches the file-based
  `./data/qatracker` database used at runtime, and every test still starts
  from a clean slate.
- `data/` (the runtime database files) is added to `.gitignore` — it's
  generated state, not source.

## Evidence: data survives a restart

Built the jar and ran it directly (not via `mvn spring-boot:run`, so the
process can be killed and restarted cleanly):

```
mvn clean package -DskipTests
java -jar target/qa-test-tracker-0.0.1-SNAPSHOT.jar
```

**1. Created a test case:**

```
POST http://localhost:8080/api/testcases
{
  "title": "Persistence check",
  "steps": "1. Create data 2. Restart app 3. Verify data still exists",
  "expectedResult": "Data survives the restart"
}
```

Response (`201`):
```json
{"id":1,"title":"Persistence check","steps":"1. Create data 2. Restart app 3. Verify data still exists","expectedResult":"Data survives the restart","status":"NOT_RUN"}
```

**2. Listed test cases before restart:**

```
GET http://localhost:8080/api/testcases
```
```json
[{"id":1,"title":"Persistence check","steps":"1. Create data 2. Restart app 3. Verify data still exists","expectedResult":"Data survives the restart","status":"NOT_RUN"}]
```

**3. Killed the process.** Confirmed the database file exists on disk:

```
$ ls -la data/
-rw-r--r-- 1 User 197121 28672 Sep  4 17:30 qatracker.mv.db
```

**4. Restarted the app** (`java -jar target/qa-test-tracker-0.0.1-SNAPSHOT.jar`)
and re-ran the same GET, with no data re-created:

```
GET http://localhost:8080/api/testcases
```
```json
[{"id":1,"title":"Persistence check","steps":"1. Create data 2. Restart app 3. Verify data still exists","expectedResult":"Data survives the restart","status":"NOT_RUN"}]
```

The record with `id: 1` is still there after the restart — confirming data
is now persisted to disk rather than lost when the JVM process ends.

**5. H2 console reachable for inspection:**
```
GET http://localhost:8080/h2-console/  -> 200
```
(JDBC URL to use in the console: `jdbc:h2:file:./data/qatracker`, user `sa`, no password.)
