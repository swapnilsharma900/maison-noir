package in.maisonnoir.backend.api.order.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(nullable = false, name = "variant_id")
    private String variantId; // Reference to MongoDB ProductVariantEntity._id

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "snapshot_name")
    private String snapshotName;

    @Column(name = "snapshot_image")
    private String snapshotImage;

    @Column(name = "snapshot_price", precision = 10, scale = 2)
    private BigDecimal snapshotPrice;

    @Column(name = "variant_label")
    private String variantLabel;

    @Column(name = "snapshot_category")
    private String snapshotCategory;
}
