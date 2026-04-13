package in.maisonnoir.backend.api.order.repository;

import in.maisonnoir.backend.api.order.model.entity.OrderEntity;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserIdOrderByPlacedAtDesc(Long userId);

    List<OrderEntity> findByOrderStatus(OrderStatus orderStatus);

    List<OrderEntity> findAllByOrderByPlacedAtDesc();
}
