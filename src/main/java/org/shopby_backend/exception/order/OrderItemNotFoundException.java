package org.shopby_backend.exception.order;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class OrderItemNotFoundException extends BusinessException implements ApiErrorCode {
    public OrderItemNotFoundException(Long orderItemId) {
        super("Aucun article ne correspond à la commande itemName : "+orderItemId);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ORDER_ITEM_NOT_FOUND;
    }
}
