package org.shopby_backend.exception.users;

public class UsersAlreadyExistsException extends RuntimeException {
    public UsersAlreadyExistsException(String message) {
        super(message);
    }
}
