package com.signalmap.liveSignalMap.exception;

import com.signalmap.liveSignalMap.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldHandleValidationError() throws Exception {
        Object invalidObject = new Object();

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(
                        invalidObject,
                        "auditorium"
                );

        bindingResult.addError(new FieldError(
                "auditorium",
                "name",
                "must not be blank"
        ));


        Method method = GlobalExceptionHandlerTest.class
                .getDeclaredMethod("shouldHandleValidationError");

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(
                        new org.springframework.core.MethodParameter(method, -1),
                        bindingResult
                );


        ResponseEntity<ErrorResponse> response =
                handler.handleValidation(exception);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);

        assertAll(
                () -> assertEquals(400, body.getStatus()),
                () -> assertEquals("Bad Request", body.getError()),
                () -> assertEquals("Validation failed", body.getMessage()),
                () -> assertNotNull(body.getTimestamp()),
                () -> assertNotNull(body.getDetails()),
                () -> assertEquals(
                        "must not be blank",
                        body.getDetails().get("name")
                )
        );
    }

    @Test
    void shouldHandleBssidNotFound() {
        BssidNotFoundException exception =
                new BssidNotFoundException("AA:BB:CC:DD:EE:FF");

        ResponseEntity<ErrorResponse> response =
                handler.handleBssidNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);

        assertAll(
                () -> assertEquals(404, body.getStatus()),
                () -> assertEquals("Not Found", body.getError()),
                () -> assertEquals("BSSID not found: AA:BB:CC:DD:EE:FF", body.getMessage()),
                () -> assertNotNull(body.getTimestamp()),
                () -> assertNull(body.getDetails())
        );
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception =
                new RuntimeException("Database connection failed");

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(exception);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        ErrorResponse body = response.getBody();
        assertNotNull(body);

        assertAll(
                () -> assertEquals(500, body.getStatus()),
                () -> assertEquals("Internal Server Error", body.getError()),
                () -> assertEquals("Internal server error", body.getMessage()),
                () -> assertNotNull(body.getTimestamp()),
                () -> assertNull(body.getDetails()),
                () -> assertFalse(
                        body.getMessage().contains("Database connection failed")
                )
        );
    }

    @Test
    void shouldHandleAuditoriumNotFound() {
        AuditoriumNotFoundException exception =
                new AuditoriumNotFoundException(999L);

        ResponseEntity<ErrorResponse> response =
                handler.handleAuditoriumNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);

        assertAll(
                () -> assertEquals(404, body.getStatus()),
                () -> assertEquals("Not Found", body.getError()),
                () -> assertEquals(exception.getMessage(), body.getMessage()),
                () -> assertNotNull(body.getTimestamp()),
                () -> assertNull(body.getDetails())
        );
    }
}