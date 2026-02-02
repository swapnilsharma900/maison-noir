package in.maisonnoir.backend.api.product.model.dto;

import in.maisonnoir.backend.api.common.validation.SafeText;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @SafeText
    private String image;

    @SafeText
    private String category;

    @Valid
    private List<VariantDTO> variants;

    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;
}
