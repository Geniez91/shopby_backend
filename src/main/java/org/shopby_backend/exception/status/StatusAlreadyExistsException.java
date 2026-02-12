package org.shopby_backend.exception.status;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class StatusAlreadyExistsException extends BusinessException implements ApiErrorCode {
    public StatusAlreadyExistsException(String libelle) {

        super("Le status existe deja avec le libelle : "+libelle);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.STATUS_ALREADY_EXISTS;
    }
}
