package in.maisonnoir.backend.api.order.model.entity;

import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "order_items")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
    @Id
    private String id; // MongoDB ObjectId

    @Indexed
    private Long orderId; // Reference to MySQL OrderEntity

    private ProductEntity product; // Embedded product snapshot

    private String selectedSize; // Selected variant size

    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    private BigDecimal priceAtOrder; // Price snapshot at time of order
}
