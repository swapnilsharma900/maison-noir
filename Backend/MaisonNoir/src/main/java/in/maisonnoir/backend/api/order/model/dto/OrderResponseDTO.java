package in.maisonnoir.backend.api.order.model.dto;

import in.maisonnoir.backend.api.account.model.dto.address.AddressDTO;
import in.maisonnoir.backend.api.common.item.model.dto.orderItem.OrderItemResponseDTO;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;

    private Long userId;

    private AddressDTO shippingAddress;

    private List<OrderItemResponseDTO> orderItems;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private String paymentMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
