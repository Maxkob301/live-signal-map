package com.signalmap.liveSignalMap.exception;

public class AuditoriumNotFoundException extends RuntimeException {
    public AuditoriumNotFoundException(Long id) {
        super("Auditorium not found: " + id);
    }
}