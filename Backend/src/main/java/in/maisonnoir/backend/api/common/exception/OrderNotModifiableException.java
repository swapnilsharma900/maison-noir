package in.maisonnoir.backend.api.common.exception;

import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import lombok.Getter;

/**
 * Thrown when an order cannot be cancelled or updated due to its current status.
 */
@Getter
public class OrderNotModifiableException extends RuntimeException {

    private final Long orderId;
    private final OrderStatus currentStatus;

    public OrderNotModifiableException(Long orderId, OrderStatus currentStatus, String action) {
        super(String.format("Order #%d cannot be %s — current status is '%s'",
                orderId, action, currentStatus.name()));
        this.orderId = orderId;
        this.currentStatus = currentStatus;
    }
}
