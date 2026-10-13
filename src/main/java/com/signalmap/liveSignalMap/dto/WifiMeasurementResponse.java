package com.signalmap.liveSignalMap.dto;

import com.signalmap.liveSignalMap.entity.WifiMeasurement;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WifiMeasurementResponse {

    private Long id;
    private String bssid;
    private Long auditoriumId;   // null, если BSSID не привязан
    private boolean mapped;      // ← добавили
    private Double downloadSpeed;
    private Integer ping;
    private LocalDateTime timestamp;

    public static WifiMeasurementResponse from(WifiMeasurement entity) {
        return WifiMeasurementResponse.builder()
                .id(entity.getId())
                .bssid(entity.getBssid())
                .auditoriumId(entity.getAuditoriumId())
                .mapped(entity.getAuditoriumId() != null)  // ← добавили
                .downloadSpeed(entity.getDownloadSpeed())
                .ping(entity.getPing())
                .timestamp(entity.getTimestamp())
                .build();
    }
}