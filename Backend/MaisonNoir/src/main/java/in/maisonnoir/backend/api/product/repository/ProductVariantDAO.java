package in.maisonnoir.backend.api.product.repository;

import in.maisonnoir.backend.api.product.model.entity.ProductVariantEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantDAO extends MongoRepository<ProductVariantEntity, String> {

    /** Find all variants belonging to a product. */
    List<ProductVariantEntity> findByProductId(String productId);

    @NotNull
    Optional<ProductVariantEntity> findById(@NotNull String id);

    /** Delete all variants belonging to a product. */
    void deleteByProductId(String productId);
}
