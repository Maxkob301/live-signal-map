package com.signalmap.liveSignalMap.dto;

import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Wi-Fi measurement response")
public class WifiMeasurementResponse {

    @Schema(description = "Measurement ID", example = "42")
    private Long id;

    @Schema(description = "Wi-Fi access point MAC address", example = "aa:bb:cc:dd:ee:ff")
    private String bssid;

    @Schema(description = "Resolved auditorium ID (null if BSSID is not mapped)",
            example = "5", nullable = true)
    private Long auditoriumId;

    @Schema(description = "Whether BSSID was successfully mapped to an auditorium",
            example = "true")
    private boolean mapped;

    @Schema(description = "Download speed in Mbps", example = "45.5")
    private Double downloadSpeed;

    @Schema(description = "Ping in milliseconds", example = "23")
    private Integer ping;

    @Schema(description = "Measurement timestamp (set by server)",
            example = "2026-10-14T18:00:43")
    private LocalDateTime timestamp;

    public static WifiMeasurementResponse from(WifiMeasurement entity) {
        return WifiMeasurementResponse.builder()
                .id(entity.getId())
                .bssid(entity.getBssid())
                .auditoriumId(entity.getAuditoriumId())
                .mapped(entity.getAuditoriumId() != null)
                .downloadSpeed(entity.getDownloadSpeed())
                .ping(entity.getPing())
                .timestamp(entity.getTimestamp())
                .build();
    }
}