package com.signalmap.liveSignalMap.controller;


import com.signalmap.liveSignalMap.dto.MobileMeasurementRequest;
import com.signalmap.liveSignalMap.dto.MobileMeasurementResponse;
import com.signalmap.liveSignalMap.service.MobileMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurements/mobile")
@RequiredArgsConstructor
@Tag(name = "Mobile Measurements", description = "Receive and store mobile network measurements")
public class MobileMeasurementController {

    private final MobileMeasurementService mobileMeasurementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Submit mobile measurement",
            description = "Accepts a mobile measurement. Auditorium ID should be provided by the client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurement created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public MobileMeasurementResponse create(@Valid @RequestBody MobileMeasurementRequest request){
        return mobileMeasurementService.save(request);
    }
}
