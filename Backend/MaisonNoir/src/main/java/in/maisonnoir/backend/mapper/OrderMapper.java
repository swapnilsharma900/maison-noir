package in.maisonnoir.backend.mapper;

import in.maisonnoir.backend.DTO.OrderDTO;
import in.maisonnoir.backend.entity.OrderEntity;

public class OrderMapper {

    public static OrderEntity toEntity(OrderDTO dto) {
        return OrderEntity.builder()
                .orderNumber(dto.getOrderNumber())
                .placedAt(dto.getPlacedAt())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .build();
    }

    public static OrderDTO toDTO(OrderEntity entity) {
        return OrderDTO.builder()
                .orderNumber(entity.getOrderNumber())
                .placedAt(entity.getPlacedAt())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .build();
    }
}
