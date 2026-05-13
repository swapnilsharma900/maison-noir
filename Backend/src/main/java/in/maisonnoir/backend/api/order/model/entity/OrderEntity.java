package in.maisonnoir.backend.api.order.model.entity;

import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_user_id", columnList = "user_id"),
        @Index(name = "idx_order_status", columnList = "order_status")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    // Inline shipping address (snapshot at order time)
    @Column(nullable = false, name = "ship_name")
    private String shipName;

    @Column(nullable = false, name = "ship_flat")
    private String shipFlat;

    @Column(nullable = false, name = "ship_city")
    private String shipCity;

    @Column(nullable = false, name = "ship_pincode")
    private String shipPincode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "order_status")
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(nullable = false, precision = 10, scale = 2, name = "total")
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "payment_status")
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(nullable = false, name = "payment_method")
    private String paymentMethod; // COD, CARD, UPI, etc.

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "placed_at")
    private LocalDateTime placedAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version; // Optimistic locking

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    // Helper method to check if order can be cancelled
    public boolean isCancellable() {
        return orderStatus != OrderStatus.SHIPPED
                && orderStatus != OrderStatus.DELIVERED
                && orderStatus != OrderStatus.CANCELLED;
    }

    // Helper method to check if order can be updated
    public boolean isUpdatable() {
        return orderStatus != OrderStatus.SHIPPED
                && orderStatus != OrderStatus.DELIVERED
                && orderStatus != OrderStatus.CANCELLED
                && orderStatus != OrderStatus.RETURNED;
    }
}
