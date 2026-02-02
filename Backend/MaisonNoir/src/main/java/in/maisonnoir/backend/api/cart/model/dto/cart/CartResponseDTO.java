package in.maisonnoir.backend.api.cart.model.dto.cart;

import in.maisonnoir.backend.api.cart.model.dto.cartItem.CartItemResponseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {
    private Long cartId;
    private List<CartItemResponseDTO> items;
    private Integer totalItems;
    private BigDecimal totalAmount;
    private String updatedAt;
}
