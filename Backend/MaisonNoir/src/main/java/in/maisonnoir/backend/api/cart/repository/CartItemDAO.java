package in.maisonnoir.backend.api.cart.repository;

import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemDAO extends MongoRepository<CartItemEntity, String> {

    List<CartItemEntity> findByCartId(Long cartId);

    @NotNull Optional<CartItemEntity> findById(@NotNull String id);

    void deleteById(@NotNull String id);

    void deleteByCartId(Long cartId);
}
