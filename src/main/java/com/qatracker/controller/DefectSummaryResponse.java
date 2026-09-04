package com.qatracker.controller;

import java.util.Map;

public class DefectSummaryResponse {
    private long total;
    private Map<String, Long> bySeverity;
    private Map<String, Long> byStatus;

    public DefectSummaryResponse(long total, Map<String, Long> bySeverity, Map<String, Long> byStatus) {
        this.total = total;
        this.bySeverity = bySeverity;
        this.byStatus = byStatus;
    }

    public long getTotal() {
        return total;
    }

    public Map<String, Long> getBySeverity() {
        return bySeverity;
    }

    public Map<String, Long> getByStatus() {
        return byStatus;
    }
}
