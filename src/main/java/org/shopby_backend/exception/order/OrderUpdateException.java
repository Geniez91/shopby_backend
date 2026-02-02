package org.shopby_backend.exception.order;

public class OrderUpdateException extends RuntimeException {
    public OrderUpdateException(String message) {
        super(message);
    }
}
