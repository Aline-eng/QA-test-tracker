package com.qatracker.service;

import com.qatracker.model.Defect;
import com.qatracker.model.Severity;
import com.qatracker.repository.DefectRepository;
import com.qatracker.repository.TestCaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class DefectServiceTest {
    private DefectService defectService;
    private TestCaseService testCaseService;

    @BeforeEach
    void setUp() {
        testCaseService = new TestCaseService(new TestCaseRepository());
        defectService = new DefectService(new DefectRepository(), testCaseService);
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
}
