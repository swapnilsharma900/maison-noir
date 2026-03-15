package in.maisonnoir.backend.api.common.item.model.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
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
 * Unified item entity that serves both cart and order contexts.
 *
 * <p>
 * <b>Cart context</b>: {@code cartId} is set, {@code orderId} is null.
 * Price is resolved dynamically from the master product at read-time.
 * </p>
 *
 * <p>
 * <b>Order context</b>: {@code orderId} is set, {@code cartId} is null.
 * {@code totalPrice} (unitPrice × quantity) is frozen at checkout and never changes.
 * </p>
 */
@Document(collection = "item_collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntity {

    @Id
    @Field("_id")
    private String itemId;

    /** Reference to MySQL Cart — null when item belongs to an order. */
    @Indexed
    private Long cartId;

    /** Reference to MySQL Order — null when item is still in a cart. */
    @Indexed
    private Long orderId;


    /** Lightweight snapshot of essential product details for display. */
    private ProductSnapshot productSnapshot;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 10, message = "Quantity must be at most 10")
    private Integer quantity;

    /**
     * Frozen total price at the time the order was placed (unitPrice × quantity).
     * <p>
     * Null while the item is in the cart (price is resolved dynamically).
     * </p>
     */
    @Positive(message = "Total price must be positive")
    private BigDecimal totalPrice;

    /**
     * Embedded lightweight snapshot containing only essential product details
     * needed for display (name, image, category, selected size).
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductSnapshot {
        private String productId;
        private String productName;
        private String productImage;
        private String productCategory;
        private String selectedSize;
        private BigDecimal productPrice;
    }
}
