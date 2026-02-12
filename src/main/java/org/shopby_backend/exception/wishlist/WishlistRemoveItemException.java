package org.shopby_backend.exception.wishlist;

import org.shopby_backend.exception.BusinessException;

public class WishlistRemoveItemException extends BusinessException {
    public WishlistRemoveItemException(Long idWishList,Long idArticle) {

        super("L'id de liste d'envie et de l'article ne correpond a aucun article d'une liste d'envie avec idWishlist :"+idWishList+ " et idArticle : "+idArticle);
    }
}
