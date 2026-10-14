package com.signalmap.liveSignalMap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Schema(description = "Standard error response")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP status reason", example = "Bad Request")
    private String error;

    @Schema(description = "Human-readable error message", example = "Validation failed")
    private String message;

    @Schema(description = "Error timestamp", example = "2026-10-14T18:00:43")
    private LocalDateTime timestamp;

    @Schema(description = "Field-level validation errors (only for 400)",
            nullable = true)
    private Map<String, String> details;

    public static ErrorResponse of(HttpStatus status, String message, Map<String, String> details) {
        ErrorResponseBuilder builder = builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now());

        if (details != null && !details.isEmpty()) {
            builder.details(details);
        }

        return builder.build();
    }
}