package in.maisonnoir.backend.api.common.item.mapper;

import in.maisonnoir.backend.api.common.item.model.dto.ProductSnapshotDTO;
import in.maisonnoir.backend.api.common.item.model.dto.orderItem.OrderItemResponseDTO;
import in.maisonnoir.backend.api.common.item.model.entity.ItemEntity;

import java.math.BigDecimal;

/**
 * Mapper for order-context item operations.
 * Handles conversion between ItemEntity and order item DTOs,
 * and the cart-to-order transition logic.
 */
public class OrderItemMapper {

    /**
     * Convert a unified ItemEntity (in order context) to the order item response DTO.
     * Uses the frozen totalPrice.
     */
    public static OrderItemResponseDTO toResponse(ItemEntity entity) {
        if (entity == null)
            return null;

        return OrderItemResponseDTO.builder()
                .itemId(entity.getItemId())
                .product(toSnapshotDTO(entity.getProductSnapshot()))
                .quantity(entity.getQuantity())
                .totalPrice(entity.getTotalPrice())
                .build();
    }

    /**
     * Transition a cart item into an order item by freezing the price and
     * reassigning ownership from cartId to orderId.
     */
    public static void transitionToOrderItem(ItemEntity item, Long orderId) {
        BigDecimal unitPrice = item.getProductSnapshot().getProductPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        item.setTotalPrice(totalPrice); // freeze total price (unitPrice × quantity)
        item.setOrderId(orderId);
        item.setCartId(null); // detach from cart
    }

    /**
     * Convert an entity-level ProductSnapshot to the DTO.
     */
    private static ProductSnapshotDTO toSnapshotDTO(ItemEntity.ProductSnapshot snapshot) {
        if (snapshot == null)
            return null;

        return ProductSnapshotDTO.builder()
                .productId(snapshot.getProductId())
                .productName(snapshot.getProductName())
                .productImage(snapshot.getProductImage())
                .productCategory(snapshot.getProductCategory())
                .selectedSize(snapshot.getSelectedSize())
                .productPrice(snapshot.getProductPrice())
                .build();
    }
}
