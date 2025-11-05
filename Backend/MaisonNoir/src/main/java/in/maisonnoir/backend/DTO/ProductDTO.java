package in.maisonnoir.backend.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull
    @DecimalMin(value = "0.0", message = "Price must be Positive")
    private Double price;

    private String image;

    private String category;

    private Boolean isAvailable;
}
