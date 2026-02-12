package org.shopby_backend.exception.users;

import org.shopby_backend.exception.BusinessException;

public class NewPasswordException extends BusinessException {
    public NewPasswordException(String email) {

        super("L'email ne correspond a aucun utilisateur avec l'email "+email);
    }
}
