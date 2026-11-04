package com.signalmap.liveSignalMap.repository;

import com.signalmap.liveSignalMap.AbstractIntegrationTest;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

class WifiMeasurementRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WifiMeasurementRepository wifiMeasurementRepository;

    @Test
    void shouldInitializeTimestampsWhenPersisted() {
        WifiMeasurement measurement = WifiMeasurement.builder()
                .bssid("aa:bb:cc:dd:ee:ff")
                .downloadSpeed(45.5)
                .uploadSpeed(12.3)
                .ping(23)
                .build();

        assertNull(measurement.getTimestamp());
        assertNull(measurement.getCreatedAt());


        WifiMeasurement saved = wifiMeasurementRepository.saveAndFlush(measurement);


        assertNotNull(saved.getId());
        assertNotNull(saved.getTimestamp());
        assertNotNull(saved.getCreatedAt());

        WifiMeasurement found = wifiMeasurementRepository
                .findById(saved.getId())
                .orElseThrow();

        assertTrue(
                Duration.between(saved.getTimestamp(), found.getTimestamp())
                        .abs()
                        .compareTo(Duration.ofNanos(1000)) <= 0
        );

        assertTrue(
                Duration.between(saved.getCreatedAt(), found.getCreatedAt())
                        .abs()
                        .compareTo(Duration.ofNanos(1000)) <= 0
        );
    }
}