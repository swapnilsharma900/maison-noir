package in.maisonnoir.backend.api.product.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "product_collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @Field("_id")
    private String productId;

    @Indexed
    private String productName;

    @Size(max = 1000, message = "Product Description should not be more than 1000 characters")
    private String productDescription;

    @Positive(message = "Price must be positive")
    private BigDecimal productPrice;

    private String productImage;

    @Indexed
    private String productCategory;

    @Builder.Default
    private List<ProductVariant> productVariants = new ArrayList<>(); // Size variations

    private Integer productStock; // Total available stock across all variants

    private Double productRating; // Calculated average rating

    private Integer productReviews; // Total number of reviews

    @LastModifiedDate
    private LocalDateTime updatedAt;


    // Embedded class for product variants (sizes)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductVariant {
        private String variantSize; // e.g., "S", "M", "L", "XL"
        private Integer variantStock; // Stock for this specific size
        private BigDecimal variantPriceAdjustment; // Additional price for this size (optional, default 0)
    }
}
