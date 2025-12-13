package in.maisonnoir.backend.api.cart.repository;

import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemDAO extends JpaRepository<CartItemEntity,Long> {
}
