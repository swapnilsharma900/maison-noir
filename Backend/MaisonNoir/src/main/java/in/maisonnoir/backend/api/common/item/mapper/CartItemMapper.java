package in.maisonnoir.backend.api.common.item.mapper;

import in.maisonnoir.backend.api.common.item.model.dto.ProductSnapshotDTO;
import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemAddDTO;
import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemResponseDTO;
import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemUpdateDTO;
import in.maisonnoir.backend.api.common.item.model.entity.ItemEntity;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;

import java.math.BigDecimal;

/**
 * Mapper for cart-context item operations.
 * Handles conversion between ItemEntity and cart item DTOs.
 */
public class CartItemMapper {

    /**
     * Convert a unified ItemEntity (in cart context) to the cart item response DTO.
     * The price is derived dynamically from the embedded productSnapshot.
     */
    public static CartItemResponseDTO toResponse(ItemEntity entity) {
        if (entity == null)
            return null;

        BigDecimal unitPrice = entity.getProductSnapshot().getProductPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(entity.getQuantity()));

        return CartItemResponseDTO.builder()
                .itemId(entity.getItemId())
                .product(toSnapshotDTO(entity.getProductSnapshot()))
                .quantity(entity.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }

    /**
     * Create a new ItemEntity in cart context from the add-to-cart DTO.
     */
    public static ItemEntity toEntity(CartItemAddDTO dto, Long cartId, ProductEntity product) {
        if (dto == null)
            return null;

        return ItemEntity.builder()
                .cartId(cartId)
                .productSnapshot(buildSnapshot(product, dto.getSelectedSize()))
                .quantity(dto.getQuantity())
                .build();
    }

    /**
     * Apply quantity updates from the update DTO onto the existing item.
     */
    public static void applyUpdate(ItemEntity item, CartItemUpdateDTO dto) {
        item.setQuantity(dto.getQuantity());
    }

    /**
     * Build a lightweight ProductSnapshot from the full ProductEntity.
     */
    public static ItemEntity.ProductSnapshot buildSnapshot(ProductEntity product, String selectedSize) {
        BigDecimal finalPrice = product.getProductPrice();
        if (selectedSize != null && product.getProductVariants() != null) {
            for (ProductEntity.ProductVariant variant : product.getProductVariants()) {
                if (selectedSize.equals(variant.getVariantSize()) && variant.getVariantPriceAdjustment() != null) {
                    finalPrice = finalPrice.add(variant.getVariantPriceAdjustment());
                    break;
                }
            }
        }

        return ItemEntity.ProductSnapshot.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productImage(product.getProductImage())
                .productCategory(product.getProductCategory())
                .selectedSize(selectedSize)
                .productPrice(finalPrice)
                .build();
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
