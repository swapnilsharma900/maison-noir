package in.maisonnoir.backend.api.cart.mapper;

import in.maisonnoir.backend.api.cart.model.dto.cartItem.CartItemAddDTO;
import in.maisonnoir.backend.api.cart.model.dto.cartItem.CartItemResponseDTO;
import in.maisonnoir.backend.api.cart.model.dto.cartItem.CartItemUpdateDTO;
import in.maisonnoir.backend.api.cart.model.dto.cart.CartResponseDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.product.mapper.ProductMapper;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    // cart response
    // toCartResponse()
    public static CartResponseDTO toResponse(CartEntity entity, List<CartItemEntity> cartItems) {
        if (entity == null) return null;

        List<CartItemResponseDTO> itemDTOs = cartItems.stream()
                .map(CartMapper::toItemResponse)
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

    // cart item response
    public static CartItemResponseDTO toItemResponse(CartItemEntity entity) {
        if (entity == null) return null;

        return CartItemResponseDTO.builder()
                .itemId(entity.getItemId())
                .product(ProductMapper.toResponse(entity.getProduct()))
                .quantity(entity.getQuantity())
                .totalPrice(entity.getTotalPrice())
                .build();
    }

    // for adding new cart item
    public static CartItemEntity toItemEntity(CartItemAddDTO dto, Long cartId, ProductEntity product) {
        if (dto == null) return null;

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity() ));

        return CartItemEntity.builder()
                .itemId(product.getId())
                .cartId(cartId)
                .product(product)
                .quantity(dto.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }

    // for updating existing cart item
    public static void applyUpdate(CartItemEntity item, CartItemUpdateDTO dto) {
        item.setQuantity(dto.getQuantity());
    }
}





