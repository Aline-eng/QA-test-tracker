package com.qatracker.service;

import com.qatracker.model.TestCase;
import com.qatracker.model.TestStatus;
import com.qatracker.repository.TestCaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class TestCaseService {

    private static final Logger log = LoggerFactory.getLogger(TestCaseService.class);

    private final TestCaseRepository repository;

    public TestCaseService(TestCaseRepository repository) {
        this.repository = repository;
    }

    // Story #1: create a test case
    public TestCase createTestCase(String title, String steps, String expectedResult) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (steps == null || steps.isBlank()) {
            throw new IllegalArgumentException("Steps are required");
        }
        if (expectedResult == null || expectedResult.isBlank()) {
            throw new IllegalArgumentException("Expected result is required");
        }
        TestCase testCase = new TestCase(null, title, steps, expectedResult);
        TestCase saved = repository.save(testCase);
        log.info("Created test case id={} title='{}'", saved.getId(), saved.getTitle());
        return saved;
    }

    // Story #2: list all test cases
    public Collection<TestCase> getAllTestCases() {
        return repository.findAll();
    }

    public TestCase getTestCaseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Test case " + id + " not found"));
    }

    // Story #3: update test case status after execution
    public TestCase updateStatus(Long id, TestStatus newStatus) {
        TestCase testCase = getTestCaseById(id);
        TestStatus oldStatus = testCase.getStatus();
        testCase.setStatus(newStatus);
        TestCase saved = repository.save(testCase);
        log.info("Test case id={} status changed {} -> {}", id, oldStatus, newStatus);
        return saved;
    }

    // Story #5: summary report of pass/fail counts
    public long countByStatus(TestStatus status) {
        return repository.findAll().stream()
                .filter(tc -> tc.getStatus() == status)
                .count();
    }

    public long countAll() {
        return repository.findAll().size();
    }
}
