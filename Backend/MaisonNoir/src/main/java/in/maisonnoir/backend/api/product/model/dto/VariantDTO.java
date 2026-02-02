package in.maisonnoir.backend.api.product.model.dto;

import in.maisonnoir.backend.api.common.validation.SafeText;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantDTO {
    @NotBlank(message = "Size is required")
    @SafeText
    private String size;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @DecimalMin(value = "0.00", message = "Price adjustment cannot be negative")
    private BigDecimal priceAdjustment;
}
