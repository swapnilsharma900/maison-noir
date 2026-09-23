package in.maisonnoir.backend.api.order.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {
    private Long id;
    private String variantId;
    private Integer quantity;
    private String snapshotName;
    private String snapshotImage;
    private BigDecimal snapshotPrice;
    private String variantLabel;
    private String snapshotCategory;
}
