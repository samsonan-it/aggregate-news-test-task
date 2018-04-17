package com.samsonan.aggregator.service.exception;

/**
 * 
 * To be used when feed not found or user doesn't have permission to access it
 */
public class RemoteNotFoundException extends Exception {

    private static final long serialVersionUID = -6277976406798780601L;

    public RemoteNotFoundException() {
    }

    public RemoteNotFoundException(String message) {
        super(message);
    }

    public RemoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteNotFoundException(Throwable cause) {
        super(cause);
    }

    public RemoteNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
