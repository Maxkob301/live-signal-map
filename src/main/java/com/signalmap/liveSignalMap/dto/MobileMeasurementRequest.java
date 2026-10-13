package com.signalmap.liveSignalMap.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MobileMeasurementRequest {

    @NotBlank(message = "Operator is required")
    @Size(max = 64, message = "Operator name is too long")
    private String operator;

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

    private Long auditoriumId;

    @Size(max = 128, message = "Device hash is too long")
    private String deviceHash;
}