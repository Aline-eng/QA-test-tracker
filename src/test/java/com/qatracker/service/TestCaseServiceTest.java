package com.qatracker.service;

import com.qatracker.model.TestCase;
import com.qatracker.model.TestStatus;
import com.qatracker.repository.TestCaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestCaseServiceTest {

    private TestCaseService service;

    @BeforeEach
    void setUp() {
        service = new TestCaseService(new TestCaseRepository());
    }

    // Story #1 acceptance criteria
    @Test
    void createTestCase_withValidData_createsWithNotRunStatus() {
        TestCase created = service.createTestCase("Login test", "1. Open app 2. Enter creds", "User logs in");

        assertNotNull(created.getId());
        assertEquals("Login test", created.getTitle());
        assertEquals(TestStatus.NOT_RUN, created.getStatus());
    }

    @Test
    void createTestCase_withBlankTitle_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createTestCase("", "some steps", "some result"));
    }

    // Story #2 acceptance criteria
    @Test
    void getAllTestCases_returnsAllCreatedCases() {
        service.createTestCase("Test A", "steps A", "result A");
        service.createTestCase("Test B", "steps B", "result B");

        assertEquals(2, service.getAllTestCases().size());
    }

    // Story #3 acceptance criteria
    @Test
    void updateStatus_forExistingTestCase_updatesStatus() {
        TestCase created = service.createTestCase("Test A", "steps A", "result A");

        TestCase updated = service.updateStatus(created.getId(), TestStatus.PASS);

        assertEquals(TestStatus.PASS, updated.getStatus());
    }

    @Test
    void updateStatus_forNonExistentTestCase_throwsException() {
        assertThrows(NoSuchElementException.class,
                () -> service.updateStatus(999L, TestStatus.PASS));
    }
    // Story #5 acceptance criteria
    @Test
    void countAll_reflectsNumberOfTestCasesCreated() {
        service.createTestCase("Test A", "steps A", "result A");
        service.createTestCase("Test B", "steps B", "result B");

        assertEquals(2, service.countAll());
    }

    @Test
    void countByStatus_reflectsCorrectStatusCounts() {
        var tc1 = service.createTestCase("Test A", "steps A", "result A");
        var tc2 = service.createTestCase("Test B", "steps B", "result B");
        service.updateStatus(tc1.getId(), TestStatus.PASS);
        service.updateStatus(tc2.getId(), TestStatus.FAIL);

        assertEquals(1, service.countByStatus(TestStatus.PASS));
        assertEquals(1, service.countByStatus(TestStatus.FAIL));
        assertEquals(0, service.countByStatus(TestStatus.BLOCKED));
    }
}
