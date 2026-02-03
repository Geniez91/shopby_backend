package org.shopby_backend.exception.users;

public class NewPasswordException extends RuntimeException {
    public NewPasswordException(String message) {
        super(message);
    }
}
