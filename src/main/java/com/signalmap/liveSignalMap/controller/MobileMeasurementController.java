package com.signalmap.liveSignalMap.controller;


import com.signalmap.liveSignalMap.dto.MobileMeasurementRequest;
import com.signalmap.liveSignalMap.dto.MobileMeasurementResponse;
import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import com.signalmap.liveSignalMap.repository.MobileMeasurementRepository;
import com.signalmap.liveSignalMap.service.MobileMeasurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurements/mobile")
@RequiredArgsConstructor
public class MobileMeasurementController {

    private final MobileMeasurementService mobileMeasurementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MobileMeasurementResponse create(@Valid @RequestBody MobileMeasurementRequest request){
        return mobileMeasurementService.save(request);
    }
}
