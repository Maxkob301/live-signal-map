package com.signalmap.liveSignalMap.dto;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithDetails() {
        Map<String, String> details = Map.of(
                "name", "must not be blank",
                "floor", "must not be null"
        );

        LocalDateTime before = LocalDateTime.now();

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                details
        );

        LocalDateTime after = LocalDateTime.now();

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals("Bad Request", response.getError()),
                () -> assertEquals("Validation failed", response.getMessage()),
                () -> assertEquals(details, response.getDetails()),
                () -> assertNotNull(response.getTimestamp()),
                () -> assertFalse(response.getTimestamp().isBefore(before)),
                () -> assertFalse(response.getTimestamp().isAfter(after))
        );
    }

    @Test
    void shouldCreateErrorResponseWithoutDetails() {
        ErrorResponse response = ErrorResponse.of(
                HttpStatus.NOT_FOUND,
                "Auditorium not found",
                null
        );

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Not Found", response.getError()),
                () -> assertEquals(
                        "Auditorium not found",
                        response.getMessage()
                ),
                () -> assertNotNull(response.getTimestamp()),
                () -> assertNull(response.getDetails())
        );
    }
}