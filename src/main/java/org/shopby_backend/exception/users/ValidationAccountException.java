package org.shopby_backend.exception.users;

public class ValidationAccountException extends RuntimeException {
    public ValidationAccountException(String message) {
        super(message);
    }
}
