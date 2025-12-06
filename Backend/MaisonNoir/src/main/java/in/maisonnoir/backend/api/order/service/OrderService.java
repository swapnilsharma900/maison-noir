package in.maisonnoir.backend.api.order.service;


import in.maisonnoir.backend.api.order.model.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDto);

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(Long id);

    void cancelOrder(Long id);

    OrderDTO updateStatus(Long id, String status);

}
