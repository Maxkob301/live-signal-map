package com.signalmap.liveSignalMap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Aggregated mobile metrics for one auditorium and operator")
public class MobileHeatmapDto {

    @Schema(description = "Auditorium ID", example = "5")
    private Long auditoriumId;

    @Schema(description = "Auditorium name", example = "312")
    private String auditoriumName;

    @Schema(description = "Building name", example = "Корпус А")
    private String building;

    @Schema(description = "Mobile operator", example = "MTS")
    private String operator;

    @Schema(description = "Average download speed in Mbps", example = "30.0")
    private Double avgDownloadSpeed;

    @Schema(description = "Average upload speed in Mbps", example = "6.0")
    private Double avgUploadSpeed;

    @Schema(description = "Average ping in ms", example = "25.0")
    private Double avgPing;

    @Schema(description = "Number of measurements", example = "3")
    private Long measurementCount;
}