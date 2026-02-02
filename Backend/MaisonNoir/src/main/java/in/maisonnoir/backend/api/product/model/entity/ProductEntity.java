package in.maisonnoir.backend.api.product.model.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    private String id;

    @Indexed
    private String name;

    @Size(max = 1000, message = "Product Description should not be more than 1000 characters")
    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String image;

    @Indexed
    private String category;

    @Builder.Default
    private List<ProductVariant> variants = new ArrayList<>(); // Size variations

    private Integer stock; // Total available stock across all variants

    private Double averageRating; // Calculated average rating

    private Integer totalReviews; // Total number of reviews

    @LastModifiedDate
    private LocalDateTime updatedAt;


    // Embedded class for product variants (sizes)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductVariant {
        private String size; // e.g., "S", "M", "L", "XL"
        private Integer stock; // Stock for this specific size
        private BigDecimal priceAdjustment; // Additional price for this size (optional, default 0)
    }
}
