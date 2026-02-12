package org.shopby_backend.exception.status;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.order.model.OrderStatus;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

public class StatusNotFoundException extends BusinessException implements ApiErrorCode {
    public StatusNotFoundException(String message) {
        super(message);
    }

    public static StatusNotFoundException byId(Long id){
        return new StatusNotFoundException("Aucun status n'existe avec l'id "+id);
    }

    public static StatusNotFoundException byOrderStatus(OrderStatus orderStatus){
        return new StatusNotFoundException("Aucun status ne correspond au status "+orderStatus);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.STATUS_NOT_FOUND;
    }
}
