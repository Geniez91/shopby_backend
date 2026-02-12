package org.shopby_backend.exception.brand;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class BrandAlreadyExistsException extends BusinessException implements ApiErrorCode {
    public BrandAlreadyExistsException(String nameBrand) {
        super("La Marque existe deja : brandName : "+nameBrand);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.BRAND_ALREADY_EXISTS;
    }
}
