package in.maisonnoir.backend.api.product.repository;

import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDAO extends MongoRepository<ProductEntity, String> {

    @NotNull Optional<ProductEntity> findById(@NotNull String id);

    List<ProductEntity> findByCategory(String category);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
