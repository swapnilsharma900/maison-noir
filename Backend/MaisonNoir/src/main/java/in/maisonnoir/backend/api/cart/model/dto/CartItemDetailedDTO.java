package in.maisonnoir.backend.api.cart.model.dto;

import in.maisonnoir.backend.api.product.model.entity.ProductEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDetailedDTO {
    private String productId;
    private int quantity;

    // Full product details from MongoDB
    private ProductEntity product;

    // Computed field (price * quantity)
    private double subtotal;
}
