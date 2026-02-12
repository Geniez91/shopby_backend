package org.shopby_backend.exception.users;

import org.shopby_backend.exception.BusinessException;

import java.time.Instant;

public class ValidationAccountException extends BusinessException {
    public ValidationAccountException(Instant expirationDate) {

        super("Le code d'utilisateur a expiré le "+expirationDate);
    }
}
