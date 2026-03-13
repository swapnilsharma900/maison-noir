package in.maisonnoir.backend.api.common.item.model.dto.orderItem;

import in.maisonnoir.backend.api.product.model.dto.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {
    private String itemId;

    private ProductResponseDTO product;

    private String selectedSize;

    private Integer quantity;

    private BigDecimal priceAtOrder;

    private BigDecimal itemTotal; // priceAtOrder × quantity
}
