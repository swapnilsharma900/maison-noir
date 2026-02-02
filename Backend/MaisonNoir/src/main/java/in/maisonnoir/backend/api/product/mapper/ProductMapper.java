package in.maisonnoir.backend.api.product.mapper;

import in.maisonnoir.backend.api.product.model.dto.ProductRequestDTO;
import in.maisonnoir.backend.api.product.model.dto.ProductResponseDTO;
import in.maisonnoir.backend.api.product.model.dto.VariantDTO;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductEntity toEntity(ProductRequestDTO dto) {
        if(dto == null) return null;

        return ProductEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .image(dto.getImage())
                .category(dto.getCategory())
                .variants(dto.getVariants().stream()
                        .map(ProductMapper::toVariantEntity)
                        .collect(Collectors.toList()))
                .stock(dto.getStock())
                .averageRating(0.0) // Default for new products
                .totalReviews(0) // Default for new products
                .build();
    }


    public static ProductResponseDTO toResponse(ProductEntity entity) {
        if (entity == null) return null;

        List<ProductEntity.ProductVariant> variants = Optional.ofNullable(entity.getVariants())
                .orElse(Collections.emptyList());



        return ProductResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .image(entity.getImage())
                .category(entity.getCategory())
                .variants(variants.stream()
                        .map(ProductMapper::toVariantDTO)
                        .collect(Collectors.toList()))
                .stock(entity.getStock())
                .averageRating(entity.getAverageRating())
                .totalReviews(entity.getTotalReviews())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static void applyUpdate(ProductRequestDTO dto, ProductEntity entity) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getImage() != null && !dto.getImage().isBlank()) {
            entity.setImage(dto.getImage());
        }
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) {
            entity.setCategory(dto.getCategory());
        }
        if (dto.getVariants() != null && !dto.getVariants().isEmpty()) {
            entity.setVariants(dto.getVariants().stream()
                    .map(ProductMapper::toVariantEntity)
                    .collect(Collectors.toList()));
        }
        if (dto.getStock() != null) {
            entity.setStock(dto.getStock());
        }
    }

    private static ProductEntity.ProductVariant toVariantEntity(VariantDTO dto) {
        if (dto == null) return null;

        return ProductEntity.ProductVariant.builder()
                .size(dto.getSize())
                .stock(dto.getStock())
                .priceAdjustment(dto.getPriceAdjustment() != null ?
                        dto.getPriceAdjustment() : BigDecimal.ZERO)
                .build();
    }

    private static VariantDTO toVariantDTO(ProductEntity.ProductVariant variant) {
        if (variant == null) return null;

        return VariantDTO.builder()
                .size(variant.getSize())
                .stock(variant.getStock())
                .priceAdjustment(variant.getPriceAdjustment() != null ?
                        variant.getPriceAdjustment() : BigDecimal.ZERO)
                .build();
    }
}
