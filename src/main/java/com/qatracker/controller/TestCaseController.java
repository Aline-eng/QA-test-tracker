package com.qatracker.controller;

import com.qatracker.model.TestCase;
import com.qatracker.service.TestCaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/testcases")
public class TestCaseController {

    private final TestCaseService service;

    public TestCaseController(TestCaseService service) {
        this.service = service;
    }

    // Story #1: create a test case
    @PostMapping
    public ResponseEntity<?> createTestCase(@RequestBody CreateTestCaseRequest request) {
        try {
            TestCase created = service.createTestCase(
                    request.getTitle(),
                    request.getSteps(),
                    request.getExpectedResult()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Story #2: list all test cases
    @GetMapping
    public Collection<TestCase> getAllTestCases() {
        return service.getAllTestCases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTestCase(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getTestCaseById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Story #3: update status after execution
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        try {
            TestCase updated = service.updateStatus(id, request.getStatus());
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
