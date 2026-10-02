package com.signalmap.liveSignalMap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileHeatmapDto {
    private String operator;
    private Double avgDownloadSpeed;
    private Double avgUploadSpeed;
    private Double avgPing;
    private Long measurementCount;
}