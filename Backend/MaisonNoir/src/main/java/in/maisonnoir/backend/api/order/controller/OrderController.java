package in.maisonnoir.backend.api.order.controller;

import in.maisonnoir.backend.api.common.response.ApiResponse;
import in.maisonnoir.backend.api.order.model.dto.OrderResponseDTO;
import in.maisonnoir.backend.api.order.model.dto.PlaceOrderDTO;
import in.maisonnoir.backend.api.order.model.dto.UpdateOrderStatusDTO;
import in.maisonnoir.backend.api.order.model.enums.OrderStatus;
import in.maisonnoir.backend.api.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@RequestBody @Valid PlaceOrderDTO requestDTO) {
        OrderResponseDTO response = orderService.placeOrder(requestDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Order placed successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        OrderResponseDTO response = orderService.getOrderById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Order fetched", response));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse> getMyOrders() {
        List<OrderResponseDTO> orders = orderService.getMyOrders();
        return ResponseEntity.ok(new ApiResponse(true, "Orders fetched", orders));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        OrderResponseDTO response = orderService.cancelOrder(id);
        return ResponseEntity.ok(new ApiResponse(true, "Order cancelled", response));
    }

    // ADMIN ENDPOINTS

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse(true, "Orders fetched", orders));
    }

    @GetMapping("/orderStatus.{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(new ApiResponse(true, "Orders fetched by status successfully", orders));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusDTO statusUpdateDTO) {
        OrderResponseDTO response = orderService.updateOrderStatus(id, statusUpdateDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Order status updated", response));
    }
}
