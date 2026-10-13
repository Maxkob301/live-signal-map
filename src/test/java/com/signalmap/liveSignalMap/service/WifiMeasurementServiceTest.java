package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.dto.WifiMeasurementRequest;
import com.signalmap.liveSignalMap.dto.WifiMeasurementResponse;
import com.signalmap.liveSignalMap.entity.Auditorium;
import com.signalmap.liveSignalMap.entity.BssidMapping;
import com.signalmap.liveSignalMap.repository.AuditoriumRepository;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class WifiMeasurementServiceTest extends AbstractIntegrationTest {

    @Autowired
    private WifiMeasurementService wifiMeasurementService;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private BssidMappingRepository bssidMappingRepository;

    @Test
    void shouldSaveMeasurementAndResolveAuditoriumByBssid() {
        // Arrange: создаём аудиторию и привязку BSSID
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Test Auditorium");
        auditorium.setBuilding("Main");
        Auditorium savedAuditorium = auditoriumRepository.save(auditorium);

        BssidMapping mapping = new BssidMapping();
        mapping.setBssid("AA:BB:CC:DD:EE:FF");
        mapping.setAuditoriumId(savedAuditorium.getId());
        mapping.setCreatedAt(java.time.LocalDateTime.now());
        bssidMappingRepository.save(mapping);

        WifiMeasurementRequest request = new WifiMeasurementRequest();
        request.setBssid("AA:BB:CC:DD:EE:FF");
        request.setDownloadSpeed(50.5);
        request.setUploadSpeed(10.2);
        request.setPing(15);
        request.setRssi(-60);
        request.setDeviceHash("test-hash-wifi");

        // Act
        WifiMeasurementResponse response = wifiMeasurementService.save(request);

        // Assert
        assertThat(response.getId()).isNotNull();
        assertThat(response.getBssid()).isEqualTo("AA:BB:CC:DD:EE:FF");
        assertThat(response.getAuditoriumId()).isEqualTo(savedAuditorium.getId());
        assertThat(response.isMapped()).isTrue();
        assertThat(response.getDownloadSpeed()).isEqualTo(50.5);
        assertThat(response.getPing()).isEqualTo(15);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void shouldSaveMeasurementWithoutAuditoriumWhenBssidUnknown() {
        // Arrange: BSSID, которого нет в маппинге
        WifiMeasurementRequest request = new WifiMeasurementRequest();
        request.setBssid("FF:FF:FF:FF:FF:FF");
        request.setDownloadSpeed(10.0);
        request.setUploadSpeed(5.0);
        request.setPing(100);

        // Act
        WifiMeasurementResponse response = wifiMeasurementService.save(request);

        // Assert
        assertThat(response.getId()).isNotNull();
        assertThat(response.getAuditoriumId()).isNull();
        assertThat(response.isMapped()).isFalse();
        assertThat(response.getTimestamp()).isNotNull();
    }
}