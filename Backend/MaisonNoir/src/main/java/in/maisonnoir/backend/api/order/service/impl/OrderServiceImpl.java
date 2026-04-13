package in.maisonnoir.backend.api.order.service.impl;

import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.account.repository.AddressDAO;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.cart.repository.CartItemDAO;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.order.mapper.OrderItemMapper;
import in.maisonnoir.backend.api.order.model.dto.OrderResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.PlaceOrderDTO;
import in.maisonnoir.backend.api.order.model.dto.UpdateOrderStatusDTO;
import in.maisonnoir.backend.api.order.model.entity.OrderEntity;
import in.maisonnoir.backend.api.order.model.entity.OrderItemEntity;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.repository.OrderDAO;
import in.maisonnoir.backend.api.order.repository.OrderItemDAO;
import in.maisonnoir.backend.api.order.service.OrderService;
import in.maisonnoir.backend.api.order.mapper.OrderMapper;
import in.maisonnoir.backend.api.product.model.entity.ProductVariantEntity;
import in.maisonnoir.backend.api.product.repository.ProductVariantDAO;
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
    private final OrderItemDAO orderItemDAO;
    private final CartDAO cartDAO;
    private final CartItemDAO cartItemDAO;
    private final ProductVariantDAO variantDAO;
    private final UserDAO userDAO;
    private final AddressDAO addressDAO;

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

        // Get cart
        CartEntity cart = cartDAO.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        // Validate cart has items
        List<CartItemEntity> cartItems = cartItemDAO.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order with empty cart");
        }

        // Fetch address
        AddressEntity address = addressDAO.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "userId", currentUser.getId()));

        // Create order entity
        OrderEntity order = OrderMapper.toEntity(placeOrderDTO, currentUser.getId(), address, cart);
        final OrderEntity savedOrder = orderDAO.save(order);

        // Transition cart items → order items (purely in MySQL)
        for (CartItemEntity cartItem : cartItems) {
            OrderItemEntity orderItem = OrderItemMapper.fromCartItem(cartItem, savedOrder);
            orderItemDAO.save(orderItem);

            // Decrement stock on the product variant
            variantDAO.findById(cartItem.getVariantId()).ifPresent(variant -> {
                int newStock = variant.getStockCount() - cartItem.getQuantity();
                variant.setStockCount(Math.max(newStock, 0));
                if (newStock <= 0) {
                    variant.setIsAvailable(false);
                }
                variantDAO.save(variant);
            });
        }

        // Clear cart after order placement
        cartItemDAO.deleteByCartId(cart.getId());
        cart.setTotalAmount(BigDecimal.ZERO);
        cartDAO.save(cart);

        // Re-fetch order with items for response
        OrderEntity finalOrder = orderDAO.findById(savedOrder.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", savedOrder.getId()));

        log.info("Order placed successfully: {} for user: {}", savedOrder.getId(), currentUser.getId());
        return OrderMapper.toResponse(finalOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        UserEntity currentUser = getCurrentUser();

        OrderEntity order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Verify order belongs to current user
        if (!order.getUserId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        log.info("Fetched order: {} for user: {}", orderId, currentUser.getId());
        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponseDTO> getMyOrders() {
        UserEntity currentUser = getCurrentUser();

        List<OrderEntity> orders = orderDAO.findByUserIdOrderByPlacedAtDesc(currentUser.getId());

        log.info("Fetched {} orders for user: {}", orders.size(), currentUser.getId());
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {
        UserEntity currentUser = getCurrentUser();

        OrderEntity order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Verify order belongs to current user
        if (!order.getUserId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        // Check if order can be cancelled
        if (!order.isCancellable()) {
            throw new RuntimeException("Order cannot be cancelled. Current status: " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderDAO.save(order);

        log.info("Order cancelled: {} by user: {}", orderId, currentUser.getId());
        return OrderMapper.toResponse(order);
    }

    // ADMIN SERVICES

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<OrderEntity> orders = orderDAO.findAllByOrderByPlacedAtDesc();

        log.info("Admin fetched all orders: {} items", orders.size());
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus orderStatus) {
        List<OrderEntity> orders = orderDAO.findByOrderStatus(orderStatus);

        log.info("Admin fetched orders by status {}: {} items", orderStatus, orders.size());
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusDTO updateOrderStatusDTO) {
        OrderEntity order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Enforce: updates only allowed if order is not shipped/delivered/cancelled/returned
        if (!order.isUpdatable()) {
            throw new RuntimeException("Order cannot be updated. Current status: " + order.getOrderStatus());
        }

        order.setOrderStatus(updateOrderStatusDTO.getOrderStatus());
        orderDAO.save(order);

        log.info("Admin updated order status: {} to {}", orderId, updateOrderStatusDTO.getOrderStatus());
        return OrderMapper.toResponse(order);
    }
}
