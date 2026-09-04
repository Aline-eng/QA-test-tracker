package com.qatracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qatracker.model.TestCase;
import com.qatracker.service.TestCaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestCaseController.class)
class TestCaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TestCaseService service;

    @Test
    void createTestCase_withBlankTitle_returns400WithFieldLevelJsonError() throws Exception {
        CreateTestCaseRequest request = new CreateTestCaseRequest();
        request.setTitle("");
        request.setSteps("some steps");
        request.setExpectedResult("some result");

        mockMvc.perform(post("/api/testcases")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void createTestCase_withValidData_returns201() throws Exception {
        CreateTestCaseRequest request = new CreateTestCaseRequest();
        request.setTitle("Login test");
        request.setSteps("1. Open app 2. Enter creds");
        request.setExpectedResult("User logs in");

        TestCase created = new TestCase(1L, "Login test", "1. Open app 2. Enter creds", "User logs in");
        when(service.createTestCase(any(), any(), any())).thenReturn(created);

        mockMvc.perform(post("/api/testcases")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Login test"));
    }

    @Test
    void getTestCase_forMissingId_returns404() throws Exception {
        when(service.getTestCaseById(anyLong())).thenThrow(new NoSuchElementException("Test case 999 not found"));

        mockMvc.perform(get("/api/testcases/999"))
                .andExpect(status().isNotFound());
    }
}
