package in.maisonnoir.backend.api.cart.repository;

import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemDAO extends JpaRepository<CartItemEntity, Long> {

    List<CartItemEntity> findByCartId(Long cartId);

    Optional<CartItemEntity> findByCartIdAndVariantId(Long cartId, String variantId);

    void deleteByCartId(Long cartId);
}
