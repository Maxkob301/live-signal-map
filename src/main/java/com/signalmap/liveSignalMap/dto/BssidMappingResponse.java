
package com.signalmap.liveSignalMap.dto;

import com.signalmap.liveSignalMap.entity.BssidMapping;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BssidMappingResponse {

    private Long id;
    private String bssid;
    private Long auditoriumId;
    private LocalDateTime createdAt;

    public static BssidMappingResponse from(BssidMapping entity) {
        return BssidMappingResponse.builder()
                .id(entity.getId())
                .bssid(entity.getBssid())
                .auditoriumId(entity.getAuditoriumId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
