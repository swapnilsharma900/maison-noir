package in.maisonnoir.backend.api.order.model.dto;

import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.model.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;

    private Long userId;

    // Inline shipping address
    private String shipName;
    private String shipFlat;
    private String shipCity;
    private String shipPincode;

    private List<OrderItemResponseDTO> orderItems;

    private BigDecimal total;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private String paymentMethod;

    private LocalDateTime placedAt;

    private LocalDateTime updatedAt;
}
