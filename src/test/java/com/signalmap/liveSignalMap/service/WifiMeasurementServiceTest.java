package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class WifiMeasurementServiceTest extends AbstractIntegrationTest {

    @Autowired
    private WifiMeasurementService wifiMeasurementService;

    @Test
    void shouldSaveMeasurementAndSetCreatedAt() {
        // Arrange
        WifiMeasurement measurement = new WifiMeasurement();
        measurement.setBssid("AA:BB:CC:DD:EE:FF");
        measurement.setAuditoriumId(1L);
        measurement.setDownloadSpeed(50.5);
        measurement.setUploadSpeed(10.2);
        measurement.setPing(15);
        measurement.setRssi(-60);
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setDeviceHash("test-hash-wifi");

        // Act
        WifiMeasurement saved = wifiMeasurementService.save(measurement);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getBssid()).isEqualTo("AA:BB:CC:DD:EE:FF");
        assertThat(saved.getAuditoriumId()).isEqualTo(1L);
        assertThat(saved.getDownloadSpeed()).isEqualTo(50.5);
        assertThat(saved.getUploadSpeed()).isEqualTo(10.2);
        assertThat(saved.getPing()).isEqualTo(15);
        assertThat(saved.getRssi()).isEqualTo(-60);
    }

    @Test
    void shouldAlwaysOverrideCreatedAt() {
        // Arrange
        WifiMeasurement measurement = new WifiMeasurement();
        measurement.setBssid("11:22:33:44:55:66");
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setCreatedAt(LocalDateTime.of(2000, 1, 1, 0, 0));

        // Act
        WifiMeasurement saved = wifiMeasurementService.save(measurement);

        // Assert
        assertThat(saved.getCreatedAt().getYear()).isGreaterThan(2000);
    }
}