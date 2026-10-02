package com.signalmap.liveSignalMap.service.impl;


import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import com.signalmap.liveSignalMap.repository.WifiMeasurementRepository;
import com.signalmap.liveSignalMap.service.WifiMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WifiMeasurementServiceImpl implements WifiMeasurementService {

    private final WifiMeasurementRepository wifiMeasurementRepository;

    @Override
    public WifiMeasurement save(WifiMeasurement measurement) {
        measurement.setCreatedAt(LocalDateTime.now());
        return wifiMeasurementRepository.save(measurement);
    }
}
