package com.qatracker.controller;

import com.qatracker.model.Defect;
import com.qatracker.service.DefectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
}
