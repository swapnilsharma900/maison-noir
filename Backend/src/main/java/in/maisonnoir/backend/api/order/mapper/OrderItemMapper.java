package in.maisonnoir.backend.api.order.mapper;

import in.maisonnoir.backend.api.order.model.dto.OrderItemResponseDTO;
import in.maisonnoir.backend.api.order.model.entity.OrderItemEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.order.model.entity.OrderEntity;

/**
 * Mapper for order-context item operations.
 * Handles conversion between CartItemEntity → OrderItemEntity during checkout,
 * and OrderItemEntity → OrderItemResponseDTO for responses.
 */
public class OrderItemMapper {

    /**
     * Create an OrderItemEntity from a CartItemEntity during checkout.
     * Freezes the snapshot data at order time.
     */
    public static OrderItemEntity fromCartItem(CartItemEntity cartItem, OrderEntity order) {
        return OrderItemEntity.builder()
                .order(order)
                .variantId(cartItem.getVariantId())
                .quantity(cartItem.getQuantity())
                .snapshotName(cartItem.getSnapshotName())
                .snapshotImage(cartItem.getSnapshotImage())
                .snapshotPrice(cartItem.getSnapshotPrice())
                .variantLabel(cartItem.getVariantLabel())
                .snapshotCategory(cartItem.getSnapshotCategory())
                .build();
    }

    /**
     * Convert an OrderItemEntity to the response DTO.
     */
    public static OrderItemResponseDTO toResponse(OrderItemEntity entity) {
        if (entity == null) return null;

        return OrderItemResponseDTO.builder()
                .id(entity.getId())
                .variantId(entity.getVariantId())
                .quantity(entity.getQuantity())
                .snapshotName(entity.getSnapshotName())
                .snapshotImage(entity.getSnapshotImage())
                .snapshotPrice(entity.getSnapshotPrice())
                .variantLabel(entity.getVariantLabel())
                .snapshotCategory(entity.getSnapshotCategory())
                .build();
    }
}
