package com.qatracker.controller;

import com.qatracker.model.Defect;
import com.qatracker.model.DefectStatus;
import com.qatracker.model.Severity;
import com.qatracker.service.DefectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("api/defects")
public class DefectController {
    private final DefectService service;

    public DefectController(DefectService service) {
        this.service = service;
    }

    // Story #4: log a defect linked to a failed test case
    @PostMapping
    public ResponseEntity<?> logDefect(@Valid @RequestBody CreateDefectRequest request) {
        try {
            Defect created = service.logDefect(
                    request.getTestCaseId(),
                    request.getDescription(),
                    request.getSeverity()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("Cannot log defect: " + e.getMessage());
        }
    }
    @GetMapping
    public Collection<Defect> getAllDefects() {
        return service.getAllDefects();
    }

    // Closes future improvement #1 from sprint2-retrospective.md: defect lifecycle
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody UpdateDefectStatusRequest request) {
        try {
            Defect updated = service.updateStatus(id, request.getStatus());
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Closes future improvement #2 from sprint2-retrospective.md: severity breakdown.
    // A separate endpoint (rather than folding this into TestCaseController's
    // /api/testcases/summary) keeps each summary scoped to its own resource.
    @GetMapping("/summary")
    public DefectSummaryResponse getSummary() {
        Map<String, Long> bySeverity = new LinkedHashMap<>();
        for (Severity severity : Severity.values()) {
            bySeverity.put(severity.name(), service.countBySeverity(severity));
        }
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (DefectStatus status : DefectStatus.values()) {
            byStatus.put(status.name(), service.countByStatus(status));
        }
        return new DefectSummaryResponse(service.countAll(), bySeverity, byStatus);
    }
}
