package com.qatracker.controller;

import com.qatracker.model.TestStatus;

public class UpdateStatusRequest {
    private TestStatus status;

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }
}
