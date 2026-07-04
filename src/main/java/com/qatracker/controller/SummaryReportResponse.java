package com.qatracker.controller;

public class SummaryReportResponse {
    private long total;
    private long passed;
    private long failed;
    private long blocked;
    private long notRun;

    public SummaryReportResponse(long total, long passed, long failed, long blocked, long notRun) {
        this.total = total;
        this.passed = passed;
        this.failed = failed;
        this.blocked = blocked;
        this.notRun = notRun;
    }

    public long getTotal() {
        return total;
    }

    public long getPassed() {
        return passed;
    }

    public long getFailed() {
        return failed;
    }

    public long getBlocked() {
        return blocked;
    }

    public long getNotRun() {
        return notRun;
    }
}
