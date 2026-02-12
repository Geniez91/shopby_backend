package org.shopby_backend.exception.wishlist;

import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class WishlistNotFoundException extends RuntimeException implements ApiErrorCode {
    public WishlistNotFoundException(Integer id) {
        super("L'id de la liste d'envie ne correspond a aucun liste avec l'id "+id);
    }



    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.WISHLIST_NOT_FOUND;
    }
}
