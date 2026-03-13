package in.maisonnoir.backend.api.order.mapper;

import in.maisonnoir.backend.api.account.mapper.AddressMapper;
import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.common.item.mapper.OrderItemMapper;
import in.maisonnoir.backend.api.common.item.model.dto.orderItem.OrderItemResponseDTO;
import in.maisonnoir.backend.api.common.item.model.entity.ItemEntity;
import in.maisonnoir.backend.api.order.model.dto.OrderResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.PlaceOrderDTO;
import in.maisonnoir.backend.api.order.model.entity.OrderEntity;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.model.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for order-level operations.
 * Delegates item-level mapping to {@link OrderItemMapper}.
 */
public class OrderMapper {

        public static OrderResponseDTO toResponse(OrderEntity entity, List<ItemEntity> orderItems) {
                if (entity == null)
                        return null;

                List<OrderItemResponseDTO> itemDTOs = orderItems.stream()
                                .map(OrderItemMapper::toResponse)
                                .collect(Collectors.toList());

                return OrderResponseDTO.builder()
                                .orderId(entity.getOrderId())
                                .userId(entity.getUserId())
                                .shippingAddress(AddressMapper.toDTO(entity.getShippingAddress()))
                                .orderItems(itemDTOs)
                                .totalAmount(entity.getTotalAmount())
                                .orderStatus(entity.getOrderStatus())
                                .paymentStatus(entity.getPaymentStatus())
                                .paymentMethod(entity.getPaymentMethod())
                                .createdAt(entity.getCreatedAt())
                                .updatedAt(entity.getUpdatedAt())
                                .build();
        }

        public static OrderEntity toEntity(PlaceOrderDTO dto, Long userId, AddressEntity address, CartEntity cart) {
                if (dto == null)
                        return null;

                return OrderEntity.builder()
                                .userId(userId)
                                .shippingAddress(address)
                                .itemIds(new ArrayList<>())
                                .totalAmount(cart.getTotalAmount())
                                .orderStatus(OrderStatus.PENDING)
                                .paymentStatus(PaymentStatus.PENDING)
                                .paymentMethod(dto.getPaymentMethod())
                                .build();
        }
}
