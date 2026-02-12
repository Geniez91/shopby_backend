package org.shopby_backend.exception.order;

import org.shopby_backend.exception.BusinessException;
import org.shopby_backend.order.model.OrderStatus;
import org.shopby_backend.tools.ApiErrorCode;
import org.shopby_backend.tools.ErrorCode;

import java.time.LocalDate;

public class OrderNotFoundException extends BusinessException implements ApiErrorCode {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException byOrderId(Long orderId) {
        return new OrderNotFoundException("Aucune commande ne correspond à l'id de la commande : "+orderId);
    }

    public static OrderNotFoundException byUserId(Long userId) {
        return new OrderNotFoundException("Aucune commande ne correspond pour l'id user: "+userId);
    }

    public static OrderNotFoundException byStatusAndLocalDate(OrderStatus orderStatus, LocalDate localDate) {
        return new OrderNotFoundException("Aucun commande ne correspond au filtre date order : "+localDate+" et le status order : "+orderStatus);
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ORDER_NOT_FOUND;
    }
}
