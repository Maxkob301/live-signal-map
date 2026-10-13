package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.dto.MobileMeasurementRequest;
import com.signalmap.liveSignalMap.dto.MobileMeasurementResponse;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;

public interface MobileMeasurementService {

    MobileMeasurementResponse save(MobileMeasurementRequest request);
}