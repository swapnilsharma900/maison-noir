package in.maisonnoir.backend.api.common.item.mapper;

import in.maisonnoir.backend.api.common.item.model.dto.orderItem.OrderItemResponseDTO;
import in.maisonnoir.backend.api.common.item.model.entity.ItemEntity;
import in.maisonnoir.backend.api.product.mapper.ProductMapper;

import java.math.BigDecimal;

/**
 * Mapper for order-context item operations.
 * Handles conversion between ItemEntity and order item DTOs,
 * and the cart-to-order transition logic.
 */
public class OrderItemMapper {

    /**
     * Convert a unified ItemEntity (in order context) to the order item response
     * DTO.
     * Uses the frozen priceAtOrder for the price.
     */
    public static OrderItemResponseDTO toResponse(ItemEntity entity) {
        if (entity == null)
            return null;

        BigDecimal itemTotal = entity.getPriceAtOrder()
                .multiply(BigDecimal.valueOf(entity.getQuantity()));

        return OrderItemResponseDTO.builder()
                .itemId(entity.getItemId())
                .product(ProductMapper.toResponse(entity.getProductSnapshot()))
                .selectedSize(entity.getSelectedSize())
                .quantity(entity.getQuantity())
                .priceAtOrder(entity.getPriceAtOrder())
                .itemTotal(itemTotal)
                .build();
    }

    /**
     * Transition a cart item into an order item by freezing the price and
     * reassigning ownership from cartId to orderId.
     */
    public static void transitionToOrderItem(ItemEntity item, Long orderId) {
        item.setPriceAtOrder(item.getProductSnapshot().getProductPrice()); // freeze price
        item.setOrderId(orderId);
        item.setCartId(null); // detach from cart
    }
}
