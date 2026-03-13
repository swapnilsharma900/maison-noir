package in.maisonnoir.backend.api.common.item.repository;

import in.maisonnoir.backend.api.common.item.model.entity.ItemEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDAO extends MongoRepository<ItemEntity, String> {

    /** Find all items belonging to a specific cart. */
    List<ItemEntity> findByCartId(Long cartId);

    /** Find all items belonging to a specific order. */
    List<ItemEntity> findByOrderId(Long orderId);

    @NotNull
    Optional<ItemEntity> findById(@NotNull String id);

    void deleteById(@NotNull String id);

    void deleteByCartId(Long cartId);
}
