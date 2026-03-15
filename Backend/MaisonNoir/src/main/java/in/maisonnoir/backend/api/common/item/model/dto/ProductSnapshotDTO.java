package in.maisonnoir.backend.api.common.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Lightweight product snapshot DTO for cart/order item API responses.
 * Contains only the essential product details needed for display.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSnapshotDTO {
    private String productId;
    private String productName;
    private String productImage;
    private String productCategory;
    private String selectedSize;
    private BigDecimal productPrice;
}
