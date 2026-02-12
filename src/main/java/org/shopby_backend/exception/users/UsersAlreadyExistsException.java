package org.shopby_backend.exception.users;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class UsersAlreadyExistsException extends BusinessException implements ApiErrorCode {
    public UsersAlreadyExistsException(String email) {

        super("Vos identifiants existe deja avec email : "+email);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.USER_ALREADY_EXISTS;
    }
}
