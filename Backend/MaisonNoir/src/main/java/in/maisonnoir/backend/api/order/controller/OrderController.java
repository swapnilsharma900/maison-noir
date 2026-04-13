package in.maisonnoir.backend.api.order.controller;

import in.maisonnoir.backend.api.common.response.ApiResponse;
import in.maisonnoir.backend.api.order.model.dto.OrderResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.PlaceOrderDTO;
import in.maisonnoir.backend.api.order.model.dto.UpdateOrderStatusDTO;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints for placing, viewing, and managing orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place a new order", description = "Creates a new order from the current cart contents. Cart items are frozen as order items with snapshot pricing.")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody @Valid PlaceOrderDTO requestDTO) {
        OrderResponseDTO response = orderService.placeOrder(requestDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Order placed successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Fetches a specific order belonging to the authenticated user")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        OrderResponseDTO response = orderService.getOrderById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Order fetched", response));
    }

    @GetMapping("/my-orders")
    @Operation(summary = "Get my orders", description = "Fetches all orders placed by the authenticated user, sorted by most recent first")
    public ResponseEntity<ApiResponse> getMyOrders() {
        List<OrderResponseDTO> orders = orderService.getMyOrders();
        return ResponseEntity.ok(new ApiResponse(true, "Orders fetched", orders));
    }

    @DeleteMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order", description = "Cancels an order if it has not been shipped, delivered, or already cancelled")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        OrderResponseDTO response = orderService.cancelOrder(id);
        return ResponseEntity.ok(new ApiResponse(true, "Order cancelled", response));
    }

    // ADMIN ENDPOINTS

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders (Admin)", description = "Fetches all orders in the system, sorted by most recent. Admin access only.")
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse(true, "Orders fetched", orders));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get orders by status (Admin)", description = "Filters orders by status (PENDING, CONFIRMED, SHIPPED, etc.). Admin access only.")
    public ResponseEntity<ApiResponse> getByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(new ApiResponse(true, "Orders fetched by status successfully", orders));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status (Admin)", description = "Updates the status of an order. Cannot update if already shipped/delivered/cancelled/returned. Admin access only.")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusDTO statusUpdateDTO) {
        OrderResponseDTO response = orderService.updateOrderStatus(id, statusUpdateDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Order status updated", response));
    }
}
