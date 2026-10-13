package com.signalmap.liveSignalMap.dto;

import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MobileMeasurementResponse {

    private Long id;
    private String operator;
    private Long auditoriumId;
    private Double downloadSpeed;
    private Integer ping;
    private LocalDateTime timestamp;

    public static MobileMeasurementResponse from(MobileMeasurement entity) {
        return MobileMeasurementResponse.builder()
                .id(entity.getId())
                .operator(entity.getOperator())
                .auditoriumId(entity.getAuditoriumId())
                .downloadSpeed(entity.getDownloadSpeed())
                .ping(entity.getPing())
                .timestamp(entity.getTimestamp())
                .build();
    }
}