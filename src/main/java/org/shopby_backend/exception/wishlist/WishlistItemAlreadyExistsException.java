package org.shopby_backend.exception.wishlist;

public class WishlistItemAlreadyExistsException extends RuntimeException {
    public WishlistItemAlreadyExistsException(String message) {
        super(message);
    }
}
