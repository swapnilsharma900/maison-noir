package in.maisonnoir.backend.mapper;

import in.maisonnoir.backend.DTO.ProductDTO;
import in.maisonnoir.backend.entity.ProductEntity;

public class ProductMapper {
    public static ProductEntity toEntity(ProductDTO dto) {
        return ProductEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .image(dto.getImage())
                .isAvailable(dto.getIsAvailable())
                .build();
    }

    public static ProductDTO toDTO(ProductEntity product) {
        return ProductDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .image(product.getImage())
                .isAvailable(product.getIsAvailable())
                .build();
    }
}
