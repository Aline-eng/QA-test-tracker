package com.qatracker.controller;

import jakarta.validation.constraints.NotBlank;

public class CreateTestCaseRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Steps are required")
    private String steps;
    @NotBlank(message = "Expected result is required")
    private String expectedResult;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }
}
