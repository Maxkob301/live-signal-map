package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.config.SecurityConfig;
import com.signalmap.liveSignalMap.exception.GlobalExceptionHandler;
import com.signalmap.liveSignalMap.service.WifiMeasurementService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WifiMeasurementController.class)
@Import({
        GlobalExceptionHandler.class,
        SecurityConfig.class
})
class WifiMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WifiMeasurementService wifiMeasurementService;

    @Test
    void shouldRejectInvalidBssid() throws Exception {
        String requestBody = """
                {
                    "bssid": "invalid-bssid",
                    "downloadSpeed": 45.5,
                    "uploadSpeed": 12.3,
                    "ping": 23,
                    "rssi": -65,
                    "deviceHash": "test-device-1"
                }
                """;

        mockMvc.perform(post("/api/measurements/wifi")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.bssid")
                        .value("BSSID must be in format XX:XX:XX:XX:XX:XX"))
                .andExpect(jsonPath("$.timestamp").exists());

        verifyNoInteractions(wifiMeasurementService);
    }

    @Test
    void shouldRejectNegativeDownloadSpeed() throws Exception {
        String requestBody = """
            {
                "bssid": "aa:bb:cc:dd:ee:ff",
                "downloadSpeed": -10.5,
                "uploadSpeed": 12.3,
                "ping": 23,
                "rssi": -65,
                "deviceHash": "test-device-1"
            }
            """;

        mockMvc.perform(post("/api/measurements/wifi")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.downloadSpeed")
                        .value("Download speed cannot be negative"));

        verifyNoInteractions(wifiMeasurementService);
    }

    @Test
    void shouldReturnMultipleValidationErrors() throws Exception {
        String requestBody = """
            {
                "bssid": "invalid-bssid",
                "downloadSpeed": -10.5,
                "uploadSpeed": 12.3,
                "ping": -5,
                "rssi": -150,
                "deviceHash": "test-device-1"
            }
            """;

        mockMvc.perform(post("/api/measurements/wifi")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.bssid")
                        .value("BSSID must be in format XX:XX:XX:XX:XX:XX"))
                .andExpect(jsonPath("$.details.downloadSpeed")
                        .value("Download speed cannot be negative"))
                .andExpect(jsonPath("$.details.ping")
                        .value("Ping cannot be negative"))
                .andExpect(jsonPath("$.details.rssi")
                        .value("RSSI cannot be below -120 dBm"));

        verifyNoInteractions(wifiMeasurementService);
    }
}