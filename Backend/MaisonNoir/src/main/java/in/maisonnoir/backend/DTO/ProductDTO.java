package in.maisonnoir.backend.DTO;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Product Description is Required")
    @Size(max = 1000, message = "Product Description should not be more than 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotBlank(message = "Image URL is required")
    private String image;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;
}
