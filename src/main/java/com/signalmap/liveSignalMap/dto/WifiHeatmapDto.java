package com.signalmap.liveSignalMap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WifiHeatmapDto {
    private Long auditoriumId;
    private String auditoriumName;
    private String building;
    private Double avgDownloadSpeed;
    private Double avgUploadSpeed;
    private Double avgPing;
    private Double avgRssi;
    private Long measurementCount;
}