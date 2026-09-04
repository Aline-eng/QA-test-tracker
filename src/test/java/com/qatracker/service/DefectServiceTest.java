package com.qatracker.service;

import com.qatracker.model.Defect;
import com.qatracker.model.DefectStatus;
import com.qatracker.model.Severity;
import com.qatracker.repository.DefectRepository;
import com.qatracker.repository.TestCaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest wires up real (embedded, in-memory) JPA repositories per test,
// isolated from the file-based database used at runtime, and rolls back after each test.
@DataJpaTest
public class DefectServiceTest {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private DefectRepository defectRepository;

    private DefectService defectService;
    private TestCaseService testCaseService;

    @BeforeEach
    void setUp() {
        testCaseService = new TestCaseService(testCaseRepository);
        defectService = new DefectService(defectRepository, testCaseService);
    }

    @Test
    void logDefect_forExistingTestCase_createsDefect() {
        var testCase = testCaseService.createTestCase("Login test", "steps", "expected result");

        Defect defect = defectService.logDefect(testCase.getId(), "Login button unresponsive", Severity.HIGH);

        assertNotNull(defect.getId());
        assertEquals(testCase.getId(), defect.getTestCaseId());
        assertEquals(Severity.HIGH, defect.getSeverity());
    }

    @Test
    void logDefect_forNonExistentTestCase_throwsException() {
        assertThrows(NoSuchElementException.class,
                () -> defectService.logDefect(999L, "Some issue", Severity.LOW));
    }

    @Test
    void getAllDefects_returnsAllLoggedDefects() {
        var testCase = testCaseService.createTestCase("Test A", "steps", "expected");
        defectService.logDefect(testCase.getId(), "Issue 1", Severity.LOW);
        defectService.logDefect(testCase.getId(), "Issue 2", Severity.CRITICAL);

        assertEquals(2, defectService.getAllDefects().size());
    }

    // Future improvement #1 from sprint2-retrospective.md: defect lifecycle
    @Test
    void logDefect_defaultsToOpenStatus() {
        var testCase = testCaseService.createTestCase("Test A", "steps", "expected");
        Defect defect = defectService.logDefect(testCase.getId(), "Issue 1", Severity.LOW);

        assertEquals(DefectStatus.OPEN, defect.getStatus());
    }

    @Test
    void updateStatus_forExistingDefect_updatesStatus() {
        var testCase = testCaseService.createTestCase("Test A", "steps", "expected");
        Defect defect = defectService.logDefect(testCase.getId(), "Issue 1", Severity.LOW);

        Defect updated = defectService.updateStatus(defect.getId(), DefectStatus.RESOLVED);

        assertEquals(DefectStatus.RESOLVED, updated.getStatus());
    }

    @Test
    void updateStatus_forNonExistentDefect_throwsException() {
        assertThrows(NoSuchElementException.class,
                () -> defectService.updateStatus(999L, DefectStatus.RESOLVED));
    }

    // Future improvement #2 from sprint2-retrospective.md: severity breakdown
    @Test
    void countBySeverityAndStatus_reflectCorrectCounts() {
        var testCase = testCaseService.createTestCase("Test A", "steps", "expected");
        var d1 = defectService.logDefect(testCase.getId(), "Issue 1", Severity.LOW);
        defectService.logDefect(testCase.getId(), "Issue 2", Severity.CRITICAL);
        defectService.updateStatus(d1.getId(), DefectStatus.RESOLVED);

        assertEquals(2, defectService.countAll());
        assertEquals(1, defectService.countBySeverity(Severity.LOW));
        assertEquals(1, defectService.countBySeverity(Severity.CRITICAL));
        assertEquals(0, defectService.countBySeverity(Severity.HIGH));
        assertEquals(1, defectService.countByStatus(DefectStatus.RESOLVED));
        assertEquals(1, defectService.countByStatus(DefectStatus.OPEN));
    }
}
