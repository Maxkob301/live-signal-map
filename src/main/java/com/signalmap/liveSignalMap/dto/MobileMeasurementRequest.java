package com.signalmap.liveSignalMap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Request to submit a mobile network measurement")
public class MobileMeasurementRequest {

    @Schema(description = "Mobile operator name",
            example = "MTS",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 64)
    @NotBlank(message = "Operator is required")
    @Size(max = 64, message = "Operator name is too long")
    private String operator;

    @Schema(description = "Download speed in Mbps",
            example = "25.5",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "10000")
    @NotNull(message = "Download speed is required")
    @Min(value = 0, message = "Download speed cannot be negative")
    @Max(value = 10000, message = "Download speed looks unrealistic")
    private Double downloadSpeed;

    @Schema(description = "Upload speed in Mbps",
            example = "5.5",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "10000")
    @NotNull(message = "Upload speed is required")
    @Min(value = 0, message = "Upload speed cannot be negative")
    @Max(value = 10000, message = "Upload speed looks unrealistic")
    private Double uploadSpeed;

    @Schema(description = "Ping in milliseconds",
            example = "30",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "0", maximum = "10000")
    @NotNull(message = "Ping is required")
    @Min(value = 0, message = "Ping cannot be negative")
    @Max(value = 10000, message = "Ping looks unrealistic")
    private Integer ping;

    @Schema(description = "Auditorium ID (optional; if null, measurement is not tied to a location)",
            example = "5", nullable = true)
    private Long auditoriumId;

    @Schema(description = "Anonymized device identifier",
            example = "test-device-2", maxLength = 128)
    @Size(max = 128, message = "Device hash is too long")
    private String deviceHash;
}