package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import com.signalmap.liveSignalMap.repository.HeatmapQueryRepository;
import com.signalmap.liveSignalMap.repository.MobileMeasurementRepository;
import com.signalmap.liveSignalMap.repository.WifiMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class HeatmapControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HeatmapQueryRepository heatmapRepository;  // если нужен

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private WifiMeasurementRepository wifiMeasurementRepository;

    @Autowired
    private MobileMeasurementRepository mobileMeasurementRepository;

    private LocalDateTime from;
    private LocalDateTime to;

    @BeforeEach
    void setUp() {
        wifiMeasurementRepository.deleteAll();
        mobileMeasurementRepository.deleteAll();
        auditoriumRepository.deleteAll();

        from = LocalDateTime.of(2026, 1, 1, 0, 0);
        to = LocalDateTime.of(2026, 12, 31, 23, 59);
    }

    @Test
    void shouldReturnWifiHeatmap() throws Exception {
        Auditorium auditorium = createAuditorium("312", "Корпус А");
        createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);
        createWifi(auditorium.getId(), 55.0, 11.0, 12, -58);

        mockMvc.perform(get("/api/heatmap/wifi")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].auditoriumId").value(auditorium.getId()))
                .andExpect(jsonPath("$[0].auditoriumName").value("312"))
                .andExpect(jsonPath("$[0].building").value("Корпус А"))
                .andExpect(jsonPath("$[0].measurementCount").value(3));
    }

    @Test
    void shouldReturnMobileHeatmap() throws Exception {
        Auditorium auditorium = createAuditorium("312", "Корпус А");
        createMobile(auditorium.getId(), "MTS", 25.0, 5.0, 30);
        createMobile(auditorium.getId(), "MTS", 30.0, 6.0, 25);
        createMobile(auditorium.getId(), "MTS", 35.0, 7.0, 20);

        mockMvc.perform(get("/api/heatmap/mobile")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operator").value("MTS"))
                .andExpect(jsonPath("$[0].auditoriumName").value("312"))
                .andExpect(jsonPath("$[0].measurementCount").value(3));
    }

    @Test
    void shouldFilterByTime() throws Exception {
        Auditorium auditorium = createAuditorium("312", "Корпус А");

        WifiMeasurement old = createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        old.setTimestamp(LocalDateTime.of(2025, 1, 1, 0, 0));
        wifiMeasurementRepository.save(old);

        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);
        createWifi(auditorium.getId(), 55.0, 11.0, 12, -58);
        createWifi(auditorium.getId(), 58.0, 11.0, 13, -57);

        mockMvc.perform(get("/api/heatmap/wifi")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementCount").value(3));
    }

    private Auditorium createAuditorium(String name, String building) {
        Auditorium auditorium = new Auditorium();
        auditorium.setName(name);
        auditorium.setBuilding(building);
        auditorium.setFloor(3);
        return auditoriumRepository.save(auditorium);
    }

    private WifiMeasurement createWifi(Long auditoriumId, Double download,
                                       Double upload, Integer ping, Integer rssi) {
        WifiMeasurement m = WifiMeasurement.builder()
                .bssid("AA:BB:CC:DD:EE:FF")
                .auditoriumId(auditoriumId)
                .downloadSpeed(download)
                .uploadSpeed(upload)
                .ping(ping)
                .rssi(rssi)
                .timestamp(LocalDateTime.now())
                .deviceHash("test-hash")
                .build();
        return wifiMeasurementRepository.save(m);
    }

    private MobileMeasurement createMobile(Long auditoriumId, String operator,
                                           Double download, Double upload, Integer ping) {
        MobileMeasurement m = MobileMeasurement.builder()
                .operator(operator)
                .auditoriumId(auditoriumId)
                .downloadSpeed(download)
                .uploadSpeed(upload)
                .ping(ping)
                .timestamp(LocalDateTime.now())
                .deviceHash("test-hash")
                .build();
        return mobileMeasurementRepository.save(m);
    }
}