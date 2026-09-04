package com.qatracker.controller;

import com.qatracker.model.DefectStatus;

public class UpdateDefectStatusRequest {
    private DefectStatus status;

    public DefectStatus getStatus() {
        return status;
    }

    public void setStatus(DefectStatus status) {
        this.status = status;
    }
}
