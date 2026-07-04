package com.qatracker.service;

import com.qatracker.model.Defect;
import com.qatracker.model.Severity;
import com.qatracker.repository.DefectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DefectService {
    private static final Logger log = LoggerFactory.getLogger(DefectService.class);

    private final DefectRepository defectRepository;
    private final TestCaseService testCaseService;

    public DefectService(DefectRepository defectRepository, TestCaseService testCaseService) {
        this.defectRepository = defectRepository;
        this.testCaseService = testCaseService;
    }

    // Story #4: log a defect linked to a failed test case
    public Defect logDefect(Long testCaseId, String description, Severity severity) {
        // Throws NoSuchElementException if the test case doesn't exist
        testCaseService.getTestCaseById(testCaseId);

        Defect defect = new Defect(null, testCaseId, description, severity);
        Defect saved = defectRepository.save(defect);
        log.info("Logged defect id={} for testCaseId={} severity={}", saved.getId(), testCaseId, severity);
        return saved;
    }

    public Collection<Defect> getAllDefects() {
        return defectRepository.findAll();
    }
}
