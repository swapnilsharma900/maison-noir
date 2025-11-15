package in.maisonnoir.backend.service.Impl;

import in.maisonnoir.backend.DTO.OrderDTO;
import in.maisonnoir.backend.Repository.OrderRepository;
import in.maisonnoir.backend.entity.OrderEntity;
import in.maisonnoir.backend.mapper.OrderMapper;
import in.maisonnoir.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDTO createOrder(OrderDTO dto) {
        OrderEntity entity = OrderMapper.toEntity(dto);
        entity.setPlacedAt(LocalDateTime.now());          // Timestamp still set
        OrderEntity saved = orderRepository.save(entity);
        return OrderMapper.toDTO(saved);
    }


    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDTO)
                .toList();
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderMapper.toDTO(entity);
    }

    @Override
    public void cancelOrder(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        entity.setStatus("CANCELLED");
        orderRepository.save(entity);
    }

    @Override
    public OrderDTO updateStatus(Long id, String status) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        entity.setStatus(status);
        OrderEntity updated = orderRepository.save(entity);
        return OrderMapper.toDTO(updated);
    }

}
