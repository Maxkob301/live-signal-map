package com.signalmap.liveSignalMap.service;

import com.signalmap.liveSignalMap.dto.WifiMeasurementRequest;
import com.signalmap.liveSignalMap.dto.WifiMeasurementResponse;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;

public interface WifiMeasurementService {

    WifiMeasurementResponse save(WifiMeasurementRequest request);
}