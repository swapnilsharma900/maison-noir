package in.maisonnoir.backend.api.order.model.enums;

public enum OrderStatus {
    PENDING,        // Order placed but not yet processed
    CONFIRMED,      // Payment confirmed, preparing for shipment
    SHIPPED,        // Order dispatched
    DELIVERED,      // Order delivered to customer
    CANCELLED,      // Cancelled by user or system
    RETURNED        // Returned by customer
}
