package in.maisonnoir.backend.api.cart.repository;

import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDAO extends JpaRepository<CartEntity, Long> {
//    Optional<CartEntity> findByUserId(Long userId);
}
