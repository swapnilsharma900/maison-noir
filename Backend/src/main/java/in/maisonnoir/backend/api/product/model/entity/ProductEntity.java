package in.maisonnoir.backend.api.product.model.entity;

import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    private String id;

    @Indexed
    private String name;

    @Size(max = 1000, message = "Product description should not be more than 1000 characters")
    private String description;

    @Indexed
    private String category;

    @Builder.Default
    private List<String> images = new ArrayList<>();

    /**
     * Generic variant options metadata describing available axes of variation
     * (e.g. [{label: "Size", values: ["S","M","L"]}, {label: "Color", values: ["Red","Blue"]}]).
     * Actual purchasable SKUs live in the {@code items} collection as ProductVariantEntity.
     */
    @Builder.Default
    private List<Map<String, Object>> variants = new ArrayList<>();

    /**
     * Flexible key-value attributes (e.g. material, care instructions, fit type).
     */
    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

    @Builder.Default
    @Field("is_active")
    private Boolean isActive = true;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;
}
