package com.qatracker.service;

import com.qatracker.model.Defect;
import com.qatracker.model.DefectStatus;
import com.qatracker.model.Severity;
import com.qatracker.repository.DefectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

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

    public Defect getDefectById(Long id) {
        return defectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Defect " + id + " not found"));
    }

    // Future improvement #1 from sprint2-retrospective.md: defect lifecycle
    public Defect updateStatus(Long id, DefectStatus newStatus) {
        Defect defect = getDefectById(id);
        DefectStatus oldStatus = defect.getStatus();
        defect.setStatus(newStatus);
        Defect saved = defectRepository.save(defect);
        log.info("Defect id={} status changed {} -> {}", id, oldStatus, newStatus);
        return saved;
    }

    // Future improvement #2 from sprint2-retrospective.md: severity breakdown
    public long countAll() {
        return defectRepository.count();
    }

    public long countBySeverity(Severity severity) {
        return defectRepository.findAll().stream()
                .filter(d -> d.getSeverity() == severity)
                .count();
    }

    public long countByStatus(DefectStatus status) {
        return defectRepository.findAll().stream()
                .filter(d -> d.getStatus() == status)
                .count();
    }
}
