package com.signalmap.liveSignalMap.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
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