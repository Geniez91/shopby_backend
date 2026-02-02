package org.shopby_backend.exception.order;

public class OrderGetByUserIdException extends RuntimeException {
    public OrderGetByUserIdException(String message) {
        super(message);
    }
}
