
package com.signalmap.liveSignalMap.exception;

public class AuditoriumHasDependenciesException extends RuntimeException {

    public AuditoriumHasDependenciesException(Long auditoriumId) {
        super("Cannot delete auditorium " + auditoriumId
                + ": related records exist");
    }
}
