package com.signalmap.liveSignalMap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Aggregated Wi-Fi metrics for one auditorium")
public class WifiHeatmapDto {

    @Schema(description = "Auditorium ID", example = "5")
    private Long auditoriumId;

    @Schema(description = "Auditorium name", example = "312")
    private String auditoriumName;

    @Schema(description = "Building name", example = "Корпус А")
    private String building;

    @Schema(description = "Average download speed in Mbps", example = "55.0")
    private Double avgDownloadSpeed;

    @Schema(description = "Average upload speed in Mbps", example = "11.0")
    private Double avgUploadSpeed;

    @Schema(description = "Average ping in ms", example = "12.5")
    private Double avgPing;

    @Schema(description = "Average RSSI in dBm", example = "-58.0")
    private Double avgRssi;

    @Schema(description = "Number of measurements", example = "3")
    private Long measurementCount;
}