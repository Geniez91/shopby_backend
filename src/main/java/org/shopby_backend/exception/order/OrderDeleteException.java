package org.shopby_backend.exception.order;

public class OrderDeleteException extends RuntimeException {
    public OrderDeleteException(String message) {
        super(message);
    }
}
