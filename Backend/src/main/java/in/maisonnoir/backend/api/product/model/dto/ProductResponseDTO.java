package in.maisonnoir.backend.api.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    private String id;

    private String name;

    private String description;

    private String category;

    private List<String> images;

    private List<Map<String, Object>> variants;

    private Map<String, Object> attributes;

    private Boolean isActive;

    private LocalDateTime createdAt;

    /** The purchasable SKU variants associated with this product. */
    private List<VariantItemDTO> variantItems;
}
