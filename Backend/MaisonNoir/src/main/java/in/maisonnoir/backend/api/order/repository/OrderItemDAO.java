package in.maisonnoir.backend.api.order.repository;

import in.maisonnoir.backend.api.order.model.entity.OrderItemEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemDAO extends MongoRepository<OrderItemEntity, String> {

    List<OrderItemEntity> findByOrderId(Long orderId);

    @NotNull Optional<OrderItemEntity> findById(@NotNull String id);

}
