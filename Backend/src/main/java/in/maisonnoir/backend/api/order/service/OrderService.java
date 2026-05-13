package in.maisonnoir.backend.api.order.service;


import in.maisonnoir.backend.api.order.model.dto.OrderResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.PlaceOrderDTO;
import in.maisonnoir.backend.api.order.model.dto.UpdateOrderStatusDTO;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    // CUSTOMER SERVICES
    OrderResponseDTO placeOrder(PlaceOrderDTO placeOrderDTO);

    OrderResponseDTO getOrderById(Long orderId);

    List<OrderResponseDTO> getMyOrders();

    OrderResponseDTO cancelOrder(Long orderId);

    // ADMIN SERVICES
    List<OrderResponseDTO> getAllOrders();

    List<OrderResponseDTO> getOrdersByStatus(OrderStatus orderStatus);

    OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusDTO updateOrderStatusDTO);
}