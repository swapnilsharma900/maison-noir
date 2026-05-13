package in.maisonnoir.backend.api.product.model.entity;

import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

/**
 * Represents a purchasable SKU / variant of a product in the catalog.
 * Each variant has its own price, stock, and availability.
 * Cart and order line items reference this entity's {@code _id}.
 */
@Document(collection = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantEntity {

    @Id
    private String id;

    /** Reference to the parent product in the products collection. */
    @Indexed
    @Field("product_id")
    private String productId;

    /** Human-readable variant label, e.g. "S", "M", "L", "Red". */
    @Field("variant_label")
    private String variantLabel;

    /** Display name for this variant, e.g. "Black Tee - Size M". */
    private String name;

    /** Variant-specific image URL. */
    private String image;

    /** Selling price for this specific variant. */
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    /** Denormalized category from the parent product. */
    private String category;

    /** Available inventory count. */
    @Min(value = 0, message = "Stock count cannot be negative")
    @Field("stock_count")
    private Integer stockCount;

    /** Whether this variant is available for purchase. */
    @Builder.Default
    @Field("is_available")
    private Boolean isAvailable = true;
}
