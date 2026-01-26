package org.shopby_backend.wishlist.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class WishListItemId {
    private Integer wishlistId;
    private Integer articleId;
}
