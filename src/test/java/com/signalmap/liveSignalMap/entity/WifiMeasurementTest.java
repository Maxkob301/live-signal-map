package com.signalmap.liveSignalMap.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WifiMeasurementTest {

    @Test
    void shouldInitializeTimestampsBeforePersist() {
        WifiMeasurement measurement = WifiMeasurement.builder()
                .bssid("aa:bb:cc:dd:ee:ff")
                .downloadSpeed(45.5)
                .uploadSpeed(12.3)
                .ping(23)
                .build();

        assertNull(measurement.getTimestamp());
        assertNull(measurement.getCreatedAt());

        LocalDateTime before = LocalDateTime.now();


        measurement.onCreate();

        LocalDateTime after = LocalDateTime.now();

        assertNotNull(measurement.getTimestamp());
        assertNotNull(measurement.getCreatedAt());

        assertFalse(measurement.getTimestamp().isBefore(before));
        assertFalse(measurement.getTimestamp().isAfter(after));

        assertFalse(measurement.getCreatedAt().isBefore(before));
        assertFalse(measurement.getCreatedAt().isAfter(after));
    }

    @Test
    void shouldPreserveExistingTimestampsBeforePersist() {
        LocalDateTime measurementTime =
                LocalDateTime.of(2026, 10, 20, 14, 30);

        LocalDateTime creationTime =
                LocalDateTime.of(2026, 10, 21, 9, 15);

        WifiMeasurement measurement = WifiMeasurement.builder()
                .bssid("aa:bb:cc:dd:ee:ff")
                .downloadSpeed(45.5)
                .uploadSpeed(12.3)
                .ping(23)
                .timestamp(measurementTime)
                .createdAt(creationTime)
                .build();

        measurement.onCreate();


        assertEquals(measurementTime, measurement.getTimestamp());
        assertEquals(creationTime, measurement.getCreatedAt());
    }
}