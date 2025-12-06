package in.maisonnoir.backend.api.order.mapper;

import in.maisonnoir.backend.api.order.model.dto.OrderDTO;
import in.maisonnoir.backend.api.order.model.entity.OrderEntity;
import in.maisonnoir.backend.api.order.model.entity.OrderItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderEntity toEntity(OrderDTO dto) {
        OrderEntity entity = OrderEntity.builder()
                .orderNumber(dto.getOrderNumber())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .build();

        List<OrderItemEntity> items = dto.getItems().stream()
                .map(itemDTO -> {
                    OrderItemEntity itemEntity = OrderItemMapper.toEntity(itemDTO);
                    itemEntity.setOrder(entity); // link back to parent
                    return itemEntity;
                })
                .collect(Collectors.toList());

        entity.setItems(items);
        return entity;
    }

    public static OrderDTO toDTO(OrderEntity entity) {
        return OrderDTO.builder()
                .orderNumber(entity.getOrderNumber())
                .placedAt(entity.getPlacedAt())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .items(entity.getItems().stream()
                        .map(OrderItemMapper::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
