package com.signalmap.liveSignalMap.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class WifiMeasurementRequest {

    @NotBlank(message = "BSSID is required")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$",
            message = "BSSID must be in format XX:XX:XX:XX:XX:XX")
    private String bssid;

    @NotNull(message = "Download speed is required")
    @Min(value = 0, message = "Download speed cannot be negative")
    @Max(value = 10000, message = "Download speed looks unrealistic")
    private Double downloadSpeed;

    @NotNull(message = "Upload speed is required")
    @Min(value = 0, message = "Upload speed cannot be negative")
    @Max(value = 10000, message = "Upload speed looks unrealistic")
    private Double uploadSpeed;

    @NotNull(message = "Ping is required")
    @Min(value = 0, message = "Ping cannot be negative")
    @Max(value = 10000, message = "Ping looks unrealistic")
    private Integer ping;

    @Min(value = -120, message = "RSSI cannot be below -120 dBm")
    @Max(value = 0, message = "RSSI cannot be above 0 dBm")
    private Integer rssi;

    @Size(max = 128, message = "Device hash is too long")
    private String deviceHash;
}