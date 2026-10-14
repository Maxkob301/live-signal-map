package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.dto.MobileHeatmapDto;
import com.signalmap.liveSignalMap.dto.WifiHeatmapDto;
import com.signalmap.liveSignalMap.repository.HeatmapQueryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/heatmap")
@RequiredArgsConstructor
@Tag(name = "Heatmap", description = "Aggregated signal data for map visualization")
public class HeatmapController {

    private final HeatmapQueryRepository heatmapRepository;

    @GetMapping("/wifi")
    @Operation(summary = "Get Wi-Fi heatmap",
            description = "Returns average Wi-Fi metrics per auditorium. Only auditoriums with 3+ measurements are included.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Heatmap data returned"),
            @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    public List<WifiHeatmapDto> getWifi(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return heatmapRepository.getWifiHeatmap(from, to);
    }

    @GetMapping("/mobile")
    @Operation(summary = "Get mobile heatmap",
            description = "Returns average mobile metrics per auditorium and operator. Only groups with 3+ measurements are included.")
    public List<MobileHeatmapDto> getMobile(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return heatmapRepository.getMobileHeatmap(from, to);
    }
}