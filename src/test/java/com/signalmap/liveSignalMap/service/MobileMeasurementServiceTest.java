package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MobileMeasurementServiceTest extends AbstractIntegrationTest {

    @Autowired
    private MobileMeasurementService mobileMeasurementService;

    @Test
    void shouldSaveMeasurementAndSetCreatedAt() {
        // Arrange
        MobileMeasurement measurement = new MobileMeasurement();
        measurement.setOperator("MTS");
        measurement.setDownloadSpeed(25.5);
        measurement.setUploadSpeed(5.5);
        measurement.setPing(30);
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setDeviceHash("test-hash-mobile");

        // Act
        MobileMeasurement saved = mobileMeasurementService.save(measurement);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getOperator()).isEqualTo("MTS");
        assertThat(saved.getDownloadSpeed()).isEqualTo(25.5);
        assertThat(saved.getUploadSpeed()).isEqualTo(5.5);
        assertThat(saved.getPing()).isEqualTo(30);
    }

    @Test
    void shouldAlwaysOverrideCreatedAt() {
        // Arrange
        MobileMeasurement measurement = new MobileMeasurement();
        measurement.setOperator("Beeline");
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setCreatedAt(LocalDateTime.of(2000, 1, 1, 0, 0));

        // Act
        MobileMeasurement saved = mobileMeasurementService.save(measurement);

        // Assert
        assertThat(saved.getCreatedAt().getYear()).isGreaterThan(2000);
    }
}