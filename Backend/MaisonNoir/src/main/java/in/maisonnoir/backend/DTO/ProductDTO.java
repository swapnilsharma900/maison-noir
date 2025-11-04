package in.maisonnoir.backend.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @DecimalMin(value = "0.0", message = "Price must be Positive")
    private Double price;

    private String image;

    private String category;

    private Boolean isAvailable;
}
