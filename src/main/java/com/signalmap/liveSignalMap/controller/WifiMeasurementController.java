package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import com.signalmap.liveSignalMap.service.WifiMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurements/wifi")
@RequiredArgsConstructor
public class WifiMeasurementController {

    private final WifiMeasurementService wifiMeasurementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WifiMeasurement create(@RequestBody WifiMeasurement measurement) {
        return wifiMeasurementService.save(measurement);
    }
}
