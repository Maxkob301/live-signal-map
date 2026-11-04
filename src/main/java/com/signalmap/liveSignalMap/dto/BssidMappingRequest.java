
package com.signalmap.liveSignalMap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class BssidMappingRequest {

    @NotBlank(message = "BSSID is required")
    @Pattern(
            regexp = "(?i)^([0-9a-f]{2}:){5}[0-9a-f]{2}$",
            message = "Invalid BSSID format"
    )
    private String bssid;

    @NotNull(message = "Auditorium ID is required")
    private Long auditoriumId;
}
