package in.maisonnoir.backend.api.product.model.dto;

import in.maisonnoir.backend.api.common.validation.SafeText;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for creating/updating a single product variant (SKU).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantItemDTO {

    private String id; // Only populated in responses

    @NotBlank(message = "Variant label is required")
    @SafeText
    private String variantLabel;

    @NotBlank(message = "Variant name is required")
    @SafeText
    private String name;

    @SafeText
    private String image;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @SafeText
    private String category;

    @Min(value = 0, message = "Stock count cannot be negative")
    private Integer stockCount;

    private Boolean isAvailable;
}
