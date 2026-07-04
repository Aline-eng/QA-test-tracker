package com.qatracker.service;

import com.qatracker.model.TestCase;
import com.qatracker.model.TestStatus;
import com.qatracker.repository.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class TestCaseService {

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
        return repository.save(testCase);
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
        testCase.setStatus(newStatus);
        return repository.save(testCase);
    }
}
