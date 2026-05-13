package in.maisonnoir.backend.api.cart.mapper;

import in.maisonnoir.backend.api.cart.model.dto.CartItemResponseDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.product.model.entity.ProductVariantEntity;

import java.math.BigDecimal;

/**
 * Mapper for SQL CartItemEntity operations.
 * Now operates on ProductVariantEntity (MongoDB SKU) instead of the old ItemEntity.
 */
public class CartItemMapper {

    /**
     * Create a CartItemEntity from a ProductVariantEntity and cart context.
     */
    public static CartItemEntity toEntity(CartEntity cart, ProductVariantEntity variant, int quantity) {
        return CartItemEntity.builder()
                .cart(cart)
                .variantId(variant.getId())
                .quantity(quantity)
                .snapshotName(variant.getName())
                .snapshotImage(variant.getImage())
                .snapshotPrice(variant.getPrice())
                .variantLabel(variant.getVariantLabel())
                .snapshotCategory(variant.getCategory())
                .build();
    }

    /**
     * Refresh snapshot fields from the latest variant data.
     */
    public static void refreshSnapshot(CartItemEntity cartItem, ProductVariantEntity variant) {
        cartItem.setSnapshotName(variant.getName());
        cartItem.setSnapshotImage(variant.getImage());
        cartItem.setSnapshotPrice(variant.getPrice());
        cartItem.setVariantLabel(variant.getVariantLabel());
        cartItem.setSnapshotCategory(variant.getCategory());
    }

    /**
     * Convert CartItemEntity to response DTO.
     */
    public static CartItemResponseDTO toResponse(CartItemEntity entity) {
        if (entity == null) return null;

        BigDecimal totalPrice = entity.getSnapshotPrice()
                .multiply(BigDecimal.valueOf(entity.getQuantity()));

        return CartItemResponseDTO.builder()
                .id(entity.getId())
                .variantId(entity.getVariantId())
                .quantity(entity.getQuantity())
                .snapshotName(entity.getSnapshotName())
                .snapshotImage(entity.getSnapshotImage())
                .snapshotPrice(entity.getSnapshotPrice())
                .variantLabel(entity.getVariantLabel())
                .snapshotCategory(entity.getSnapshotCategory())
                .totalPrice(totalPrice)
                .build();
    }
}
