package org.shopby_backend.exception.users;

import org.shopby_backend.exception.BusinessException;

public class ValidationNotFoundException extends BusinessException {
    public ValidationNotFoundException(String message) {
        super(message);
    }
}
