package com.qatracker.service;

import com.qatracker.model.Defect;
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
}
