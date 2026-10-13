package com.signalmap.liveSignalMap.service.impl;


import com.signalmap.liveSignalMap.dto.WifiMeasurementRequest;
import com.signalmap.liveSignalMap.dto.WifiMeasurementResponse;
import com.signalmap.liveSignalMap.entity.BssidMapping;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import com.signalmap.liveSignalMap.repository.BssidMappingRepository;
import com.signalmap.liveSignalMap.repository.WifiMeasurementRepository;
import com.signalmap.liveSignalMap.service.WifiMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WifiMeasurementServiceImpl implements WifiMeasurementService {

    private final WifiMeasurementRepository wifiMeasurementRepository;
    private final BssidMappingRepository bssidMappingRepository;

    @Override
    @Transactional
    public WifiMeasurementResponse save(WifiMeasurementRequest request) {
         Long auditoriumId = bssidMappingRepository
                 .findByBssid(request.getBssid())
                 .map(BssidMapping::getAuditoriumId)
                 .orElse(null);

        WifiMeasurement entity = WifiMeasurement.builder()
                .bssid(request.getBssid())
                .auditoriumId(auditoriumId)
                .downloadSpeed(request.getDownloadSpeed())
                .uploadSpeed(request.getUploadSpeed())
                .ping(request.getPing())
                .rssi(request.getRssi())
                .deviceHash(request.getDeviceHash())
                .createdAt(LocalDateTime.now())
                .build();
        WifiMeasurement saved = wifiMeasurementRepository.save(entity);

        return WifiMeasurementResponse.from(saved);
    }
}
