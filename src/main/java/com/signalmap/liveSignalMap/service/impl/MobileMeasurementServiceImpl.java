package com.signalmap.liveSignalMap.service.impl;

import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import com.signalmap.liveSignalMap.repository.MobileMeasurementRepository;
import com.signalmap.liveSignalMap.service.MobileMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MobileMeasurementServiceImpl implements MobileMeasurementService {

    private final MobileMeasurementRepository mobileMeasurementRepository;

    @Override
    public MobileMeasurement save(MobileMeasurement measurement) {
        measurement.setCreatedAt(LocalDateTime.now());
        return mobileMeasurementRepository.save(measurement);
    }
}