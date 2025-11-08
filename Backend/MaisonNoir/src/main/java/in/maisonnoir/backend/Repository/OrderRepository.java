package in.maisonnoir.backend.Repository;

import in.maisonnoir.backend.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {}
