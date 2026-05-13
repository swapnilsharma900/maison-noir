package in.maisonnoir.backend.api.order.mapper;

import in.maisonnoir.backend.api.address.model.entity.AddressEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.order.model.dto.OrderItemResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.OrderResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.PlaceOrderDTO;
import in.maisonnoir.backend.api.order.model.entity.OrderEntity;
import in.maisonnoir.backend.api.order.model.entity.OrderItemEntity;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.model.enums.PaymentStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for order-level operations.
 * Delegates item-level mapping to {@link OrderItemMapper}.
 */
public class OrderMapper {

    public static OrderResponseDTO toResponse(OrderEntity entity) {
        if (entity == null)
            return null;

        List<OrderItemResponseDTO> itemDTOs = entity.getOrderItems().stream()
                .map(OrderItemMapper::toResponse)
                .collect(Collectors.toList());

        return OrderResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .shipName(entity.getShipName())
                .shipFlat(entity.getShipFlat())
                .shipCity(entity.getShipCity())
                .shipPincode(entity.getShipPincode())
                .orderItems(itemDTOs)
                .total(entity.getTotal())
                .orderStatus(entity.getOrderStatus())
                .paymentStatus(entity.getPaymentStatus())
                .paymentMethod(entity.getPaymentMethod())
                .placedAt(entity.getPlacedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static OrderEntity toEntity(PlaceOrderDTO dto, Long userId, AddressEntity address, CartEntity cart) {
        if (dto == null)
            return null;

        return OrderEntity.builder()
                .userId(userId)
                .shipName(address.getLineOne()) // Use lineOne as the flat/name
                .shipFlat(address.getLineTwo()) // Use lineTwo as the flat/building
                .shipCity(address.getCity())
                .shipPincode(address.getPincode())
                .total(cart.getTotalAmount())
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(dto.getPaymentMethod())
                .build();
    }
}
