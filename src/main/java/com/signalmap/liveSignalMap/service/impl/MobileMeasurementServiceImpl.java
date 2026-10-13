package com.signalmap.liveSignalMap.service.impl;

import com.signalmap.liveSignalMap.dto.MobileMeasurementRequest;
import com.signalmap.liveSignalMap.dto.MobileMeasurementResponse;
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
    public MobileMeasurementResponse save(MobileMeasurementRequest request) {
        MobileMeasurement entity = MobileMeasurement.builder()
                .auditoriumId(request.getAuditoriumId())
                .deviceHash(request.getDeviceHash())
                .downloadSpeed(request.getDownloadSpeed())
                .uploadSpeed(request.getUploadSpeed())
                .operator(request.getOperator())
                .ping(request.getPing())
                .timestamp(LocalDateTime.now())
                .build();
        MobileMeasurement saved = mobileMeasurementRepository.save(entity);

        return MobileMeasurementResponse.from(saved);

    }
}