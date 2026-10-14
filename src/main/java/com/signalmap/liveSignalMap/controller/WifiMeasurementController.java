package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.dto.WifiMeasurementRequest;
import com.signalmap.liveSignalMap.dto.WifiMeasurementResponse;
import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import com.signalmap.liveSignalMap.service.WifiMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurements/wifi")
@RequiredArgsConstructor
@Tag(name = "Wi-Fi Measurements", description = "Receive and store Wi-Fi speed measurements")
public class WifiMeasurementController {

    private final WifiMeasurementService wifiMeasurementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Submit Wi-Fi measurement",
            description = "Accepts a Wi-Fi measurement. Auditorium is resolved automatically by BSSID if mapped.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurement created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public WifiMeasurementResponse create(@Valid @RequestBody WifiMeasurementRequest request) {
        return wifiMeasurementService.save(request);
    }
}
