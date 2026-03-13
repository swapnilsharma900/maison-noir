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
                .productName(dto.getName())
                .productDescription(dto.getDescription())
                .productPrice(dto.getPrice())
                .productImage(dto.getImage())
                .productCategory(dto.getCategory())
                .productVariants(dto.getVariants().stream()
                        .map(ProductMapper::toVariantEntity)
                        .collect(Collectors.toList()))
                .productStock(dto.getStock())
                .productRating(0.0) // Default for new products
                .productReviews(0) // Default for new products
                .build();
    }


    public static ProductResponseDTO toResponse(ProductEntity entity) {
        if (entity == null) return null;

        List<ProductEntity.ProductVariant> variants = Optional.ofNullable(entity.getProductVariants())
                .orElse(Collections.emptyList());

        return ProductResponseDTO.builder()
                .id(entity.getProductId())
                .name(entity.getProductName())
                .description(entity.getProductDescription())
                .price(entity.getProductPrice())
                .image(entity.getProductImage())
                .category(entity.getProductCategory())
                .variants(variants.stream()
                        .map(ProductMapper::toVariantDTO)
                        .collect(Collectors.toList()))
                .stock(entity.getProductStock())
                .averageRating(entity.getProductRating())
                .totalReviews(entity.getProductReviews())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static void applyUpdate(ProductRequestDTO dto, ProductEntity entity) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            entity.setProductName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            entity.setProductDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            entity.setProductPrice(dto.getPrice());
        }
        if (dto.getImage() != null && !dto.getImage().isBlank()) {
            entity.setProductImage(dto.getImage());
        }
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) {
            entity.setProductCategory(dto.getCategory());
        }
        if (dto.getVariants() != null && !dto.getVariants().isEmpty()) {
            entity.setProductVariants(dto.getVariants().stream()
                    .map(ProductMapper::toVariantEntity)
                    .collect(Collectors.toList()));
        }
        if (dto.getStock() != null) {
            entity.setProductStock(dto.getStock());
        }
    }

    private static ProductEntity.ProductVariant toVariantEntity(VariantDTO dto) {
        if (dto == null) return null;

        return ProductEntity.ProductVariant.builder()
                .variantSize(dto.getSize())
                .variantStock(dto.getStock())
                .variantPriceAdjustment(dto.getPriceAdjustment() != null ?
                        dto.getPriceAdjustment() : BigDecimal.ZERO)
                .build();
    }

    private static VariantDTO toVariantDTO(ProductEntity.ProductVariant variant) {
        if (variant == null) return null;

        return VariantDTO.builder()
                .size(variant.getVariantSize())
                .stock(variant.getVariantStock())
                .priceAdjustment(variant.getVariantPriceAdjustment() != null ?
                        variant.getVariantPriceAdjustment() : BigDecimal.ZERO)
                .build();
    }
}
