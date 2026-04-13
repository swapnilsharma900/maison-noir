package in.maisonnoir.backend.api.cart.mapper;

import in.maisonnoir.backend.api.cart.model.dto.CartItemResponseDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartResponseDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for cart-level operations.
 * Delegates item-level mapping to {@link CartItemMapper}.
 */
@Component
public class CartMapper {

    /**
     * Build a CartResponseDTO from the cart entity and its associated items.
     */
    public static CartResponseDTO toResponse(CartEntity entity, List<CartItemEntity> cartItems) {
        if (entity == null)
            return null;

        List<CartItemResponseDTO> itemDTOs = cartItems.stream()
                .map(CartItemMapper::toResponse)
                .collect(Collectors.toList());

        int totalItems = cartItems.stream()
                .mapToInt(CartItemEntity::getQuantity)
                .sum();

        return CartResponseDTO.builder()
                .id(entity.getId())
                .items(itemDTOs)
                .totalItems(totalItems)
                .totalAmount(entity.getTotalAmount())
                .updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null)
                .build();
    }
}
