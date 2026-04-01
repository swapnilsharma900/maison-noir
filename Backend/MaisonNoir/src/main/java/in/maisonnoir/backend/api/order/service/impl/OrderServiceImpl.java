package in.maisonnoir.backend.api.order.service.impl;

import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.common.item.model.entity.ItemEntity;
import in.maisonnoir.backend.api.common.item.repository.ItemDAO;
import in.maisonnoir.backend.api.order.model.dto.OrderResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.PlaceOrderDTO;
import in.maisonnoir.backend.api.order.model.dto.UpdateOrderStatusDTO;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.model.entity.OrderEntity;
import in.maisonnoir.backend.api.order.repository.OrderDAO;
import in.maisonnoir.backend.api.order.service.OrderService;
import in.maisonnoir.backend.api.order.mapper.OrderMapper;
import in.maisonnoir.backend.api.common.item.mapper.OrderItemMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;
    private final ItemDAO itemDAO;
    private final UserDAO userDAO;

    private UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    // CUSTOMER SERVICES

    @Override
    public OrderResponseDTO placeOrder(PlaceOrderDTO placeOrderDTO) {
        UserEntity currentUser = getCurrentUser();
        CartEntity cart = currentUser.getCart();

        if (cart == null) {
            throw new RuntimeException("Cart not found for user");
        }

        // Validate cart has items
        List<ItemEntity> cartItems = itemDAO.findByCartId(cart.getCartId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order with empty cart");
        }

        // Fetch address from user (one-to-one relationship)
        AddressEntity address = currentUser.getAddress();
        if (address == null) {
            throw new ResourceNotFoundException("Address", "user", currentUser.getUserId());
        }

        // Create order entity
        OrderEntity order = OrderMapper.toEntity(placeOrderDTO, currentUser.getUserId(), address, cart);
        order = orderDAO.save(order);

        // Transition cart items → order items (freeze price, reassign ownership)
        for (ItemEntity item : cartItems) {
            OrderItemMapper.transitionToOrderItem(item, order.getOrderId());
            itemDAO.save(item);
            order.getItemIds().add(item.getItemId());
        }

        orderDAO.save(order); // Update with order item IDs

        // Clear cart after order placement
        cart.getItemIds().clear();
        cart.setTotalAmount(BigDecimal.ZERO);

        log.info("Order placed successfully: {} for user: {}", order.getOrderId(), currentUser.getUserId());
        return OrderMapper.toResponse(order, cartItems);
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        UserEntity currentUser = getCurrentUser();

        OrderEntity order = orderDAO.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Verify order belongs to current user (security check)
        if (!order.getUserId().equals(currentUser.getUserId())) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        List<ItemEntity> orderItems = itemDAO.findByOrderId(order.getOrderId());

        log.info("Fetched order: {} for user: {}", orderId, currentUser.getUserId());
        return OrderMapper.toResponse(order, orderItems);
    }

    @Override
    public List<OrderResponseDTO> getMyOrders() {
        UserEntity currentUser = getCurrentUser();

        List<OrderEntity> orders = orderDAO.findByUserIdOrderByCreatedAtDesc(currentUser.getUserId());

        log.info("Fetched {} orders for user: {}", orders.size(), currentUser.getUserId());
        return orders.stream()
                .map(order -> {
                    List<ItemEntity> orderItems = itemDAO.findByOrderId(order.getOrderId());
                    return OrderMapper.toResponse(order, orderItems);
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {
        UserEntity currentUser = getCurrentUser();

        OrderEntity order = orderDAO.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Verify order belongs to current user
        if (!order.getUserId().equals(currentUser.getUserId())) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        // Check if order can be cancelled
        if (!order.isCancellable()) {
            throw new RuntimeException("Order cannot be cancelled. Current status: " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderDAO.save(order);

        List<ItemEntity> orderItems = itemDAO.findByOrderId(order.getOrderId());

        log.info("Order cancelled: {} by user: {}", orderId, currentUser.getUserId());
        return OrderMapper.toResponse(order, orderItems);
    }

    // ADMIN SERVICES

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<OrderEntity> orders = orderDAO.findAllByOrderByCreatedAtDesc();

        log.info("Admin fetched all orders: {} items", orders.size());
        return orders.stream()
                .map(order -> {
                    List<ItemEntity> orderItems = itemDAO.findByOrderId(order.getOrderId());
                    return OrderMapper.toResponse(order, orderItems);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus orderStatus) {
        List<OrderEntity> orders = orderDAO.findByOrderStatus(orderStatus);

        log.info("Admin fetched orders by status {}: {} items", orderStatus, orders.size());
        return orders.stream()
                .map(order -> {
                    List<ItemEntity> orderItems = itemDAO.findByOrderId(order.getOrderId());
                    return OrderMapper.toResponse(order, orderItems);
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusDTO updateOrderStatusDTO) {
        OrderEntity order = orderDAO.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        order.setOrderStatus(updateOrderStatusDTO.getOrderStatus());
        orderDAO.save(order);

        List<ItemEntity> orderItems = itemDAO.findByOrderId(order.getOrderId());

        log.info("Admin updated order status: {} to {}", orderId, updateOrderStatusDTO.getOrderStatus());
        return OrderMapper.toResponse(order, orderItems);
    }

}
