package org.shopby_backend.exception.users;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class UsersNotFoundException extends BusinessException implements ApiErrorCode {
    public UsersNotFoundException(String message) {
        super(message);
    }

    public static UsersNotFoundException byUserId(Long id) {
        return new UsersNotFoundException("L'utilisateur n'existe pas avec l'id user :"+id);
    }

    public static UsersNotFoundException byEmail(String email) {
        return new UsersNotFoundException("L'utilisateur n'existe pas avec l'email :"+email);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.USER_NOT_FOUND;
    }
}
