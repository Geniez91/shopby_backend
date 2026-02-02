package org.shopby_backend.exception.order;

public class OrderCreateException extends RuntimeException {
    public OrderCreateException(String message) {
        super(message);
    }
}
