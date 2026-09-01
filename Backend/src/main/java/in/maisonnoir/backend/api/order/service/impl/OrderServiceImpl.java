package in.maisonnoir.backend.api.order.service.impl;

import in.maisonnoir.backend.api.address.model.entity.AddressEntity;
import in.maisonnoir.backend.api.user.model.entity.UserEntity;
import in.maisonnoir.backend.api.user.model.enums.AccountRole;
import in.maisonnoir.backend.api.address.repository.AddressDAO;
import in.maisonnoir.backend.api.user.repository.UserDAO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.cart.repository.CartItemDAO;
import in.maisonnoir.backend.api.common.exception.BadRequestException;
import in.maisonnoir.backend.api.common.exception.OrderNotModifiableException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", currentUser.getId()));

        // Validate cart has items
        List<CartItemEntity> cartItems = cartItemDAO.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Your cart is empty. Please add items before placing an order.");
        }

        // Fetch address
        AddressEntity address = addressDAO.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "userId", currentUser.getId()));

        // Create order entity
        OrderEntity order = OrderMapper.toEntity(placeOrderDTO, currentUser.getId(), address, cart);
        final OrderEntity savedOrder = orderDAO.save(order);

        // Transition cart items → order items (purely in MySQL)
        // Validate stock availability for all cart items before processing
        for (CartItemEntity cartItem : cartItems) {
            ProductVariantEntity variant = variantDAO.findById(cartItem.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductVariant", "id", cartItem.getVariantId()));
            if (cartItem.getQuantity() > variant.getStockCount()) {
                throw new BadRequestException("Insufficient stock for product variant: " + variant.getId()
                        + " (requested: " + cartItem.getQuantity() + ", available: " + variant.getStockCount() + ")");
            }
        }

        // Transition cart items → order items and decrement stock
        for (CartItemEntity cartItem : cartItems) {
            OrderItemEntity orderItem = OrderItemMapper.fromCartItem(cartItem, savedOrder);
            orderItemDAO.save(orderItem);

            // Decrement stock on the product variant
            variantDAO.findById(cartItem.getVariantId()).ifPresent(variant -> {
                int newStock = variant.getStockCount() - cartItem.getQuantity();
                variant.setStockCount(newStock);
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

        // Customers may only view their own orders; admins may view any order
        boolean isAdmin = currentUser.getRole() == AccountRole.ADMIN;
        if (!isAdmin && !order.getUserId().equals(currentUser.getId())) {
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
            throw new OrderNotModifiableException(orderId, order.getOrderStatus(), "cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderDAO.save(order);

        // Restore inventory for all items in the cancelled order
        restoreInventory(order.getId());

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
            throw new OrderNotModifiableException(orderId, order.getOrderStatus(), "updated");
        }

        OrderStatus newStatus = updateOrderStatusDTO.getOrderStatus();
        order.setOrderStatus(newStatus);
        orderDAO.save(order);

        // Restore inventory if admin sets status to CANCELLED or RETURNED
        if (newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.RETURNED) {
            restoreInventory(order.getId());
        }

        log.info("Admin updated order status: {} to {}", orderId, newStatus);
        return OrderMapper.toResponse(order);
    }

    /**
     * Restores inventory for all items in a cancelled or returned order.
     * Re-adds the ordered quantity back to each variant's stock count
     * and marks the variant as available again.
     */
    private void restoreInventory(Long orderId) {
        List<OrderItemEntity> items = orderItemDAO.findByOrderId(orderId);
        for (OrderItemEntity item : items) {
            variantDAO.findById(item.getVariantId()).ifPresent(variant -> {
                variant.setStockCount(variant.getStockCount() + item.getQuantity());
                variant.setIsAvailable(true);
                variantDAO.save(variant);
            });
        }
        log.info("Inventory restored for order: {} ({} line items)", orderId, items.size());
    }
}
