package in.maisonnoir.backend.api.cart.mapper;

import in.maisonnoir.backend.api.common.item.mapper.CartItemMapper;
import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemResponseDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartResponseDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.common.item.model.entity.ItemEntity;
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
    public static CartResponseDTO toResponse(CartEntity entity, List<ItemEntity> cartItems) {
        if (entity == null)
            return null;

        List<CartItemResponseDTO> itemDTOs = cartItems.stream()
                .map(CartItemMapper::toResponse)
                .collect(Collectors.toList());

        int totalItems = itemDTOs.stream()
                .mapToInt(CartItemResponseDTO::getQuantity)
                .sum();

        return CartResponseDTO.builder()
                .cartId(entity.getCartId())
                .items(itemDTOs)
                .totalItems(totalItems)
                .totalAmount(entity.getTotalAmount())
                .updatedAt(entity.getUpdatedAt().toString())
                .build();
    }
}
