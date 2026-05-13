package in.maisonnoir.backend.api.cart.model.dto;

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
    private Long id;
    private String variantId;
    private Integer quantity;
    private String snapshotName;
    private String snapshotImage;
    private BigDecimal snapshotPrice;
    private String variantLabel;
    private String snapshotCategory;
    private BigDecimal totalPrice; // snapshotPrice × quantity
}
