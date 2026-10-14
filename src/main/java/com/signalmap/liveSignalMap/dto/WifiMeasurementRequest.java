package com.signalmap.liveSignalMap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Request to submit a Wi-Fi measurement")
public class WifiMeasurementRequest {

    @Schema(
            description = "Wi-Fi access point MAC address",
            example = "aa:bb:cc:dd:ee:ff",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "BSSID is required")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$",
            message = "BSSID must be in format XX:XX:XX:XX:XX:XX")
    private String bssid;

    @Schema(
            description = "Download speed in Mbps",
            example = "45.5",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0",
            maximum = "10000"
    )
    @NotNull(message = "Download speed is required")
    @Min(value = 0, message = "Download speed cannot be negative")
    @Max(value = 10000, message = "Download speed looks unrealistic")
    private Double downloadSpeed;

    @Schema(
            description = "Upload speed in Mbps",
            example = "12.3",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0",
            maximum = "10000"
    )
    @NotNull(message = "Upload speed is required")
    @Min(value = 0, message = "Upload speed cannot be negative")
    @Max(value = 10000, message = "Upload speed looks unrealistic")
    private Double uploadSpeed;

    @Schema(
            description = "Ping in milliseconds",
            example = "23",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0",
            maximum = "10000"
    )
    @NotNull(message = "Ping is required")
    @Min(value = 0, message = "Ping cannot be negative")
    @Max(value = 10000, message = "Ping looks unrealistic")
    private Integer ping;

    @Schema(
            description = "Wi-Fi signal strength in dBm (negative value)",
            example = "-65",
            minimum = "-120",
            maximum = "0"
    )
    @Min(value = -120, message = "RSSI cannot be below -120 dBm")
    @Max(value = 0, message = "RSSI cannot be above 0 dBm")
    private Integer rssi;

    @Schema(
            description = "Anonymized device identifier (no personal data)",
            example = "test-device-1",
            maxLength = 128
    )
    @Size(max = 128, message = "Device hash is too long")
    private String deviceHash;
}