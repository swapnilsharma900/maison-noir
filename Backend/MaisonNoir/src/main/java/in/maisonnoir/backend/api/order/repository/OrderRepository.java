package in.maisonnoir.backend.api.order.repository;

import in.maisonnoir.backend.api.order.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {}
