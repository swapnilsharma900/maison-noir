package in.maisonnoir.backend.api.product.model.dto;

import in.maisonnoir.backend.api.common.validation.SafeText;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDTO {
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    @SafeText
    private String name;

    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    @SafeText
    private String description;

    @SafeText
    private String category;

    private List<String> images;

    /** Generic variant metadata (e.g. [{label: "Size", values: ["S","M","L"]}]). */
    private List<Map<String, Object>> variants;

    /** Flexible product attributes (e.g. material, care instructions). */
    private Map<String, Object> attributes;

    /** SKU variant items to create alongside this product. */
    @Valid
    private List<VariantItemDTO> variantItems;
}
