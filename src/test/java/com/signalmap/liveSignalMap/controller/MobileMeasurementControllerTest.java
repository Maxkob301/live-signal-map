
package com.signalmap.liveSignalMap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.signalmap.liveSignalMap.config.SecurityConfig;
import com.signalmap.liveSignalMap.dto.MobileMeasurementRequest;
import com.signalmap.liveSignalMap.dto.MobileMeasurementResponse;
import com.signalmap.liveSignalMap.exception.GlobalExceptionHandler;
import com.signalmap.liveSignalMap.service.MobileMeasurementService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MobileMeasurementController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class MobileMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MobileMeasurementService mobileMeasurementService;

    @Test
    void shouldCreateMobileMeasurement() throws Exception {
        MobileMeasurementRequest request = new MobileMeasurementRequest();
        request.setOperator("MTS");
        request.setDownloadSpeed(25.5);
        request.setUploadSpeed(5.5);
        request.setPing(30);
        request.setAuditoriumId(5L);
        request.setDeviceHash("test-device-2");

        MobileMeasurementResponse response =
                MobileMeasurementResponse.builder()
                        .id(7L)
                        .operator("MTS")
                        .auditoriumId(5L)
                        .downloadSpeed(25.5)
                        .ping(30)
                        .timestamp(LocalDateTime.of(2026, 10, 28, 13, 0))
                        .build();

        when(mobileMeasurementService.save(any(MobileMeasurementRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/measurements/mobile")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.operator").value("MTS"))
                .andExpect(jsonPath("$.auditoriumId").value(5))
                .andExpect(jsonPath("$.downloadSpeed").value(25.5))
                .andExpect(jsonPath("$.ping").value(30))
                .andExpect(jsonPath("$.timestamp")
                        .value("2026-10-28T13:00:00"));

        verify(mobileMeasurementService, times(1))
                .save(any(MobileMeasurementRequest.class));
    }

    @Test
    void shouldRejectNegativeDownloadSpeed() throws Exception {
        MobileMeasurementRequest request = new MobileMeasurementRequest();
        request.setOperator("MTS");
        request.setDownloadSpeed(-10.0);
        request.setUploadSpeed(5.5);
        request.setPing(30);
        request.setAuditoriumId(5L);

        mockMvc.perform(post("/api/measurements/mobile")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.downloadSpeed")
                        .value("Download speed cannot be negative"));

        verifyNoInteractions(mobileMeasurementService);
    }

    @Test
    void shouldReturnMultipleValidationErrors() throws Exception {
        MobileMeasurementRequest request = new MobileMeasurementRequest();
        request.setOperator("");
        request.setDownloadSpeed(-10.0);
        request.setUploadSpeed(-5.0);
        request.setPing(-20);

        mockMvc.perform(post("/api/measurements/mobile")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.details.operator")
                        .value("Operator is required"))
                .andExpect(jsonPath("$.details.downloadSpeed")
                        .value("Download speed cannot be negative"))
                .andExpect(jsonPath("$.details.uploadSpeed")
                        .value("Upload speed cannot be negative"))
                .andExpect(jsonPath("$.details.ping")
                        .value("Ping cannot be negative"));

        verifyNoInteractions(mobileMeasurementService);
    }
}
