package com.signalmap.liveSignalMap.exception;


public class BssidAlreadyExistsException extends RuntimeException {

    public BssidAlreadyExistsException(String bssid) {
        super("BSSID already exists: " + bssid);
    }
}

