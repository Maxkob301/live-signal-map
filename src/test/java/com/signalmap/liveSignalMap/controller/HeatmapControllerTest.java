package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
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
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private WifiMeasurementRepository wifiMeasurementRepository;

    @Autowired
    private MobileMeasurementRepository mobileMeasurementRepository;

    private static final String FROM = "2026-01-01T00:00:00";
    private static final String TO = "2026-12-31T23:59:59";

    @BeforeEach
    void setUp() {
        wifiMeasurementRepository.deleteAll();
        mobileMeasurementRepository.deleteAll();
        auditoriumRepository.deleteAll();
    }

    @Test
    void shouldReturnEmptyWifiHeatmap() throws Exception {
        mockMvc.perform(get("/api/heatmap/wifi")
                        .param("from", FROM)
                        .param("to", TO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldReturnWifiHeatmap() throws Exception {
        Auditorium auditorium = createAuditorium();
        createWifi(auditorium.getId(), 50.0, 10.0, 15, -60);
        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);

        mockMvc.perform(get("/api/heatmap/wifi")
                        .param("from", FROM)
                        .param("to", TO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].auditoriumId").value(auditorium.getId()))
                .andExpect(jsonPath("$[0].auditoriumName").value("312"))
                .andExpect(jsonPath("$[0].avgDownloadSpeed").value(55.0))
                .andExpect(jsonPath("$[0].measurementCount").value(2));
    }

    @Test
    void shouldReturnMobileHeatmap() throws Exception {
        createMobile("MTS", 25.0, 5.0, 30);
        createMobile("MTS", 30.0, 6.0, 25);

        mockMvc.perform(get("/api/heatmap/mobile")
                        .param("from", FROM)
                        .param("to", TO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].operator").value("MTS"))
                .andExpect(jsonPath("$[0].avgDownloadSpeed").value(27.5))
                .andExpect(jsonPath("$[0].measurementCount").value(2));
    }

    @Test
    void shouldFilterByTime() throws Exception {
        Auditorium auditorium = createAuditorium();

        WifiMeasurement old = new WifiMeasurement();
        old.setBssid("AA:BB:CC:DD:EE:FF");
        old.setAuditoriumId(auditorium.getId());
        old.setDownloadSpeed(50.0);
        old.setTimestamp(LocalDateTime.of(2025, 1, 1, 0, 0));
        old.setCreatedAt(LocalDateTime.now());
        wifiMeasurementRepository.save(old);

        createWifi(auditorium.getId(), 60.0, 12.0, 10, -55);

        mockMvc.perform(get("/api/heatmap/wifi")
                        .param("from", FROM)
                        .param("to", TO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementCount").value(1));
    }

    private Auditorium createAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("312");
        auditorium.setBuilding("Корпус А");
        auditorium.setFloor(3);
        return auditoriumRepository.save(auditorium);
    }

    private WifiMeasurement createWifi(Long auditoriumId, Double download,
                                       Double upload, Integer ping, Integer rssi) {
        WifiMeasurement m = new WifiMeasurement();
        m.setBssid("AA:BB:CC:DD:EE:FF");
        m.setAuditoriumId(auditoriumId);
        m.setDownloadSpeed(download);
        m.setUploadSpeed(upload);
        m.setPing(ping);
        m.setRssi(rssi);
        m.setTimestamp(LocalDateTime.now());
        m.setCreatedAt(LocalDateTime.now());
        return wifiMeasurementRepository.save(m);
    }

    private MobileMeasurement createMobile(String operator, Double download,
                                           Double upload, Integer ping) {
        MobileMeasurement m = new MobileMeasurement();
        m.setOperator(operator);
        m.setDownloadSpeed(download);
        m.setUploadSpeed(upload);
        m.setPing(ping);
        m.setTimestamp(LocalDateTime.now());
        m.setCreatedAt(LocalDateTime.now());
        return mobileMeasurementRepository.save(m);
    }
}
