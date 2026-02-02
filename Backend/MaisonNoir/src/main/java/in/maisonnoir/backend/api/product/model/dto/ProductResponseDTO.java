package in.maisonnoir.backend.api.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private String image;

    private String category;

    private List<VariantDTO> variants;

    private Integer stock;

    private Double averageRating;

    private Integer totalReviews;

    private LocalDateTime updatedAt;
}
