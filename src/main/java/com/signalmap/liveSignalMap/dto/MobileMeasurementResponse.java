package com.signalmap.liveSignalMap.dto;

import com.signalmap.liveSignalMap.entity.MobileMeasurement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Mobile measurement response")
public class MobileMeasurementResponse {

    @Schema(description = "Measurement ID", example = "7")
    private Long id;

    @Schema(description = "Mobile operator name", example = "MTS")
    private String operator;

    @Schema(description = "Auditorium ID (null if not provided)", example = "5", nullable = true)
    private Long auditoriumId;

    @Schema(description = "Download speed in Mbps", example = "25.5")
    private Double downloadSpeed;

    @Schema(description = "Ping in milliseconds", example = "30")
    private Integer ping;

    @Schema(description = "Measurement timestamp (set by server)",
            example = "2026-10-14T18:00:43")
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