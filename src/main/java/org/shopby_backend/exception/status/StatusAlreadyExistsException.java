package org.shopby_backend.exception.status;

public class StatusAlreadyExistsException extends RuntimeException {
    public StatusAlreadyExistsException(String message) {
        super(message);
    }
}
