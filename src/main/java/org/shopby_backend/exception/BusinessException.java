package org.shopby_backend.exception;

import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public abstract class BusinessException extends RuntimeException implements ApiErrorCode {
    public BusinessException(String message) {
        super(message);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ARTICLE_ALREADY_EXISTS;
    }


}
