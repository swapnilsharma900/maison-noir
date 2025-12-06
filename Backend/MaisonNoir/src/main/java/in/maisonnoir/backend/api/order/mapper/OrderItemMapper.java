package in.maisonnoir.backend.api.order.mapper;

import in.maisonnoir.backend.api.order.model.dto.OrderItemDTO;
import in.maisonnoir.backend.api.order.model.entity.OrderItemEntity;

public class OrderItemMapper {

    public static OrderItemEntity toEntity(OrderItemDTO dto) {
        return OrderItemEntity.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }

    public static OrderItemDTO toDTO(OrderItemEntity entity) {
        return OrderItemDTO.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .build();
    }
}
