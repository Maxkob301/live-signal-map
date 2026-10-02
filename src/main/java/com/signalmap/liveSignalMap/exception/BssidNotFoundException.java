package com.signalmap.liveSignalMap.exception;

public class BssidNotFoundException extends RuntimeException {
    public BssidNotFoundException(String bssid) {
        super("BSSID not found: " + bssid);
    }
}