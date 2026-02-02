package in.maisonnoir.backend.api.order.mapper;

import in.maisonnoir.backend.api.account.mapper.AddressMapper;
import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.order.model.dto.OrderItemResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.OrderResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.PlaceOrderDTO;
import in.maisonnoir.backend.api.order.model.entity.OrderEntity;
import in.maisonnoir.backend.api.order.model.entity.OrderItemEntity;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.model.enums.PaymentStatus;
import in.maisonnoir.backend.api.product.mapper.ProductMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderResponseDTO toResponse(OrderEntity entity, List<OrderItemEntity> orderItems) {
        if (entity == null) return null;

        List<OrderItemResponseDTO> itemDTOs = orderItems.stream()
                .map(OrderMapper::toOrderItemResponse)
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
        if (dto == null) return null;

        return OrderEntity.builder()
                .userId(userId)
                .shippingAddress(address)
                .orderItemIds(new ArrayList<>())
                .totalAmount(cart.getTotalAmount())
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(dto.getPaymentMethod())
                .build();
    }

    public static OrderItemResponseDTO toOrderItemResponse(OrderItemEntity entity) {
        if (entity == null) return null;

        BigDecimal itemTotal = entity.getPriceAtOrder()
                .multiply(BigDecimal.valueOf(entity.getQuantity()));

        return OrderItemResponseDTO.builder()
                .itemId(entity.getId())
                .product(ProductMapper.toResponse(entity.getProduct()))
                .selectedSize(entity.getSelectedSize())
                .quantity(entity.getQuantity())
                .priceAtOrder(entity.getPriceAtOrder())
                .itemTotal(itemTotal)
                .build();
    }

    public static OrderItemEntity toOrderItemEntity(CartItemEntity cartItem, Long orderId) {
        if (cartItem == null) return null;

        return OrderItemEntity.builder()
                .orderId(orderId)
                .product(cartItem.getProduct()) // Product snapshot
                .selectedSize(cartItem.getProduct().getVariants().isEmpty() ? "N/A" :
                        cartItem.getProduct().getVariants().get(0).getSize()) // Default to first variant
                .quantity(cartItem.getQuantity())
                .priceAtOrder(cartItem.getProduct().getPrice())
                .build();
    }
}
