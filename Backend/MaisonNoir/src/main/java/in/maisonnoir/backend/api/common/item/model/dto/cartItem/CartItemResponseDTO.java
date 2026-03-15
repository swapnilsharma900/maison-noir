package in.maisonnoir.backend.api.common.item.model.dto.cartItem;

import in.maisonnoir.backend.api.common.item.model.dto.ProductSnapshotDTO;
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
public class CartItemResponseDTO {
    private String itemId;
    private ProductSnapshotDTO product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
