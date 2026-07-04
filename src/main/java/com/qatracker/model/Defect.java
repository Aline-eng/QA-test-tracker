package com.qatracker.model;

public class Defect {
    private Long id;
    private Long testCaseId;
    private String description;
    private Severity severity;

    public Defect() {
    }

    public Defect(Long id, Long testCaseId, String description, Severity severity) {
        this.id = id;
        this.testCaseId = testCaseId;
        this.description = description;
        this.severity = severity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
}
