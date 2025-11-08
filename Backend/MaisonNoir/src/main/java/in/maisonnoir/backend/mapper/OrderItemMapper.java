package in.maisonnoir.backend.mapper;

import in.maisonnoir.backend.DTO.OrderItemDTO;
import in.maisonnoir.backend.entity.OrderItemEntity;

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
