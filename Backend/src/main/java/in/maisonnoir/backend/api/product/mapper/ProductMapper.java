package in.maisonnoir.backend.api.product.mapper;

import in.maisonnoir.backend.api.product.model.dto.ProductRequestDTO;
import in.maisonnoir.backend.api.product.model.dto.ProductResponseDTO;
import in.maisonnoir.backend.api.product.model.dto.VariantItemDTO;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import in.maisonnoir.backend.api.product.model.entity.ProductVariantEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductMapper {

    // ───────────────── Product ─────────────────

    public static ProductEntity toEntity(ProductRequestDTO dto) {
        if (dto == null) return null;

        return ProductEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .images(dto.getImages() != null ? dto.getImages() : Collections.emptyList())
                .variants(dto.getVariants() != null ? dto.getVariants() : Collections.emptyList())
                .attributes(dto.getAttributes() != null ? dto.getAttributes() : Collections.emptyMap())
                .isActive(true)
                .build();
    }

    public static ProductResponseDTO toResponse(ProductEntity entity, List<ProductVariantEntity> variantEntities) {
        if (entity == null) return null;

        List<VariantItemDTO> variantItems = Optional.ofNullable(variantEntities)
                .orElse(Collections.emptyList())
                .stream()
                .map(ProductMapper::toVariantItemDTO)
                .collect(Collectors.toList());

        return ProductResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .images(entity.getImages())
                .variants(entity.getVariants())
                .attributes(entity.getAttributes())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .variantItems(variantItems)
                .build();
    }

    public static void applyUpdate(ProductRequestDTO dto, ProductEntity entity) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) {
            entity.setCategory(dto.getCategory());
        }
        if (dto.getImages() != null) {
            entity.setImages(dto.getImages());
        }
        if (dto.getVariants() != null) {
            entity.setVariants(dto.getVariants());
        }
        if (dto.getAttributes() != null) {
            entity.setAttributes(dto.getAttributes());
        }
    }

    // ───────────────── Variant Items ─────────────────

    public static ProductVariantEntity toVariantEntity(VariantItemDTO dto, String productId, String category) {
        if (dto == null) return null;

        return ProductVariantEntity.builder()
                .productId(productId)
                .variantLabel(dto.getVariantLabel())
                .name(dto.getName())
                .image(dto.getImage())
                .price(dto.getPrice())
                .category(category) // denormalized from parent product
                .stockCount(dto.getStockCount())
                .isAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true)
                .build();
    }

    public static VariantItemDTO toVariantItemDTO(ProductVariantEntity entity) {
        if (entity == null) return null;

        return VariantItemDTO.builder()
                .id(entity.getId())
                .variantLabel(entity.getVariantLabel())
                .name(entity.getName())
                .image(entity.getImage())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .stockCount(entity.getStockCount())
                .isAvailable(entity.getIsAvailable())
                .build();
    }
}
