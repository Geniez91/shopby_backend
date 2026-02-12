package org.shopby_backend.exception.wishlist;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class WishlistItemAlreadyExistsException extends BusinessException implements ApiErrorCode {
    public WishlistItemAlreadyExistsException(Long idWishList,Long idArticle) {
        super("L'article est deja dans votre liste d'envie avec l'id wishlist : "+idWishList + "et l'id article : "+idArticle);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.WISHLIST_ITEM_ALREADY_EXISTS;
    }
}
