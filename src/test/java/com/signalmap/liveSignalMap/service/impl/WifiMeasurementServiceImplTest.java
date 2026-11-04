package com.signalmap.liveSignalMap.service.impl;

import com.signalmap.liveSignalMap.dto.WifiMeasurementRequest;
import com.signalmap.liveSignalMap.entity.BssidMapping;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;
import com.signalmap.liveSignalMap.repository.WifiMeasurementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WifiMeasurementServiceImplTest {

    private WifiMeasurementRepository wifiMeasurementRepository;
    private BssidMappingRepository bssidMappingRepository;
    private WifiMeasurementServiceImpl service;

    @BeforeEach
    void setUp() {
        wifiMeasurementRepository = mock(WifiMeasurementRepository.class);
        bssidMappingRepository = mock(BssidMappingRepository.class);

        service = new WifiMeasurementServiceImpl(
                wifiMeasurementRepository,
                bssidMappingRepository
        );
    }

    @Test
    void shouldSaveWifiMeasurementWithMappedAuditorium() {
        WifiMeasurementRequest request = new WifiMeasurementRequest();
        request.setBssid("aa:bb:cc:dd:ee:ff");
        request.setDownloadSpeed(45.5);
        request.setUploadSpeed(12.3);
        request.setPing(23);
        request.setRssi(-65);
        request.setDeviceHash("test-device-1");

        BssidMapping mapping = mock(BssidMapping.class);
        when(mapping.getAuditoriumId()).thenReturn(101L);

        when(bssidMappingRepository.findByBssid("aa:bb:cc:dd:ee:ff"))
                .thenReturn(Optional.of(mapping));

        when(wifiMeasurementRepository.save(any(WifiMeasurement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        service.save(request);


        ArgumentCaptor<WifiMeasurement> captor =
                ArgumentCaptor.forClass(WifiMeasurement.class);

        verify(wifiMeasurementRepository).save(captor.capture());

        WifiMeasurement saved = captor.getValue();

        assertEquals("aa:bb:cc:dd:ee:ff", saved.getBssid());
        assertEquals(101L, saved.getAuditoriumId());
        assertEquals(45.5, saved.getDownloadSpeed());
        assertEquals(12.3, saved.getUploadSpeed());
        assertEquals(23, saved.getPing());
        assertEquals(-65, saved.getRssi());
        assertEquals("test-device-1", saved.getDeviceHash());
        assertNotNull(saved.getCreatedAt());

        verify(bssidMappingRepository)
                .findByBssid("aa:bb:cc:dd:ee:ff");

        verify(wifiMeasurementRepository, times(1))
                .save(any(WifiMeasurement.class));
    }

    @Test
    void shouldSaveWifiMeasurementWithoutMappedAuditorium() {
        WifiMeasurementRequest request = new WifiMeasurementRequest();
        request.setBssid("11:22:33:44:55:66");
        request.setDownloadSpeed(30.0);
        request.setUploadSpeed(10.0);
        request.setPing(35);
        request.setRssi(-70);
        request.setDeviceHash("test-device-2");

        when(bssidMappingRepository.findByBssid("11:22:33:44:55:66"))
                .thenReturn(Optional.empty());

        when(wifiMeasurementRepository.save(any(WifiMeasurement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        service.save(request);


        ArgumentCaptor<WifiMeasurement> captor =
                ArgumentCaptor.forClass(WifiMeasurement.class);

        verify(wifiMeasurementRepository).save(captor.capture());

        WifiMeasurement saved = captor.getValue();

        assertEquals("11:22:33:44:55:66", saved.getBssid());
        assertNull(saved.getAuditoriumId());
        assertEquals(30.0, saved.getDownloadSpeed());
        assertEquals(10.0, saved.getUploadSpeed());
        assertEquals(35, saved.getPing());
        assertEquals(-70, saved.getRssi());
        assertEquals("test-device-2", saved.getDeviceHash());
        assertNotNull(saved.getCreatedAt());

        verify(bssidMappingRepository)
                .findByBssid("11:22:33:44:55:66");

        verify(wifiMeasurementRepository, times(1))
                .save(any(WifiMeasurement.class));
    }
}