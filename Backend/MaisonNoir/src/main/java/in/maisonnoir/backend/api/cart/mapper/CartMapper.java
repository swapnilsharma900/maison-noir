package in.maisonnoir.backend.api.cart.mapper;

import in.maisonnoir.backend.api.cart.model.dto.CartDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartItemDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    public CartItemDTO toDTO(CartItemEntity entity) {
        return CartItemDTO.builder()
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .build();
    }

    public CartDTO toDTO(CartEntity cart) {
        return CartDTO.builder()
                .userId(cart.getUser().getUserId())
                .items(cart.getItems().stream()
                        .map(this::toDTO)
                        .toList())
                .build();
    }

}
