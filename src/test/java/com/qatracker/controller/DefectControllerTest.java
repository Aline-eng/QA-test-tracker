package com.qatracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qatracker.model.Defect;
import com.qatracker.model.Severity;
import com.qatracker.service.DefectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DefectController.class)
class DefectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DefectService service;

    @Test
    void logDefect_withBlankDescription_returns400WithFieldLevelJsonError() throws Exception {
        CreateDefectRequest request = new CreateDefectRequest();
        request.setTestCaseId(1L);
        request.setDescription("");
        request.setSeverity(Severity.HIGH);

        mockMvc.perform(post("/api/defects")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    void logDefect_withValidData_returns201() throws Exception {
        CreateDefectRequest request = new CreateDefectRequest();
        request.setTestCaseId(1L);
        request.setDescription("Login button unresponsive");
        request.setSeverity(Severity.HIGH);

        Defect created = new Defect(1L, 1L, "Login button unresponsive", Severity.HIGH);
        when(service.logDefect(anyLong(), any(), any())).thenReturn(created);

        mockMvc.perform(post("/api/defects")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Login button unresponsive"));
    }

    @Test
    void logDefect_forNonExistentTestCase_returns400() throws Exception {
        CreateDefectRequest request = new CreateDefectRequest();
        request.setTestCaseId(999L);
        request.setDescription("Should fail");
        request.setSeverity(Severity.LOW);

        when(service.logDefect(anyLong(), any(), any()))
                .thenThrow(new NoSuchElementException("Test case 999 not found"));

        mockMvc.perform(post("/api/defects")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
