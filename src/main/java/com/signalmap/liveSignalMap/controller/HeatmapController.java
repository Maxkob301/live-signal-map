package com.signalmap.liveSignalMap.controller;

import com.signalmap.liveSignalMap.dto.MobileHeatmapDto;
import com.signalmap.liveSignalMap.dto.WifiHeatmapDto;
import com.signalmap.liveSignalMap.repository.HeatmapQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/heatmap")
@RequiredArgsConstructor
public class HeatmapController {

    private final HeatmapQueryRepository heatmapRepository;

    @GetMapping("/wifi")
    public List<WifiHeatmapDto> getWifi(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return heatmapRepository.getWifiHeatmap(from, to);
    }

    @GetMapping("/mobile")
    public List<MobileHeatmapDto> getMobile(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return heatmapRepository.getMobileHeatmap(from, to);
    }
}