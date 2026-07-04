# Manual Test Script (run in this exact order for demos)

Run `mvn spring-boot:run` first. Base URL: `http://localhost:8080`

## 1. Create a test case
`POST /api/testcases`
```json
{
  "title": "Login with valid credentials",
  "steps": "1. Navigate to login page\n2. Enter username: admin@test.com\n3. Enter password: Test123!\n4. Click Login button",
  "expectedResult": "User is logged in successfully and redirected to dashboard"
}
```
Expect: `201`, status `NOT_RUN`, note the returned `id` (used below, assume `1`)

## 2. Create a second test case
`POST /api/testcases`
```json
{ 
  "title": "Login with invalid password", 
  "steps": "1. Open login page 2. Enter wrong password 3. Click Login", 
  "expectedResult": "Error message is shown"
}
```
Expect: `201`, note the returned `id` (assume `2`)

## 3. List all test cases
`GET /api/testcases`
Expect: `200`, array with the 2 test cases above

## 4. Update test case 1 to PASS
`PUT /api/testcases/1/status`
```json
{ "status": "PASS" }
```
Expect: `200`, status now `PASS`

## 5. Update test case 2 to FAIL
`PUT /api/testcases/2/status`
```json
{ "status": "FAIL" }
```
Expect: `200`, status now `FAIL`

## 6. Log a defect against the failed test case
`POST /api/defects`
```json
{ 
  "testCaseId": 2, 
  "description": "Error message does not appear on invalid password", 
  "severity": "HIGH"
}
```
Expect: `201`, defect returned with `testCaseId: 2`

## 7. Attempt to log a defect against a non-existent test case (negative test)
`POST /api/defects`
```json
{ 
  "testCaseId": 999, 
  "description": "Should fail", 
  "severity": "LOW"
}
```
Expect: `400` with an error message

## 8. List all defects
`GET /api/defects`
Expect: `200`, array with 1 defect

## 9. View summary report
`GET /api/testcases/summary`
Expect: `200`, `{ "total": 2, "passed": 1, "failed": 1, "blocked": 0, "notRun": 0 }`

## 10. Check health endpoint
`GET /actuator/health`
Expect: `200`, `{ "status": "UP", ... }`

## 11. Negative test: create a test case with missing title
`POST /api/testcases`
```json
{ 
  "title": "", 
  "steps": "some steps", 
  "expectedResult": "some result"
}
```
Expect: `400` with field-level validation error for `title`
