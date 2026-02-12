package org.shopby_backend.exception.brand;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class BrandNotFoundException extends BusinessException implements ApiErrorCode {
    public BrandNotFoundException(Long idBrand) {
        super("Aucune marque ne correspond à l'id de la marque : "+idBrand );
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.BRAND_NOT_FOUND;
    }
}
