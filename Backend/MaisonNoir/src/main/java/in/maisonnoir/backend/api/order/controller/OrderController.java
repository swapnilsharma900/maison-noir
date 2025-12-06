package in.maisonnoir.backend.api.order.controller;

import in.maisonnoir.backend.api.order.model.dto.OrderDTO;
import in.maisonnoir.backend.payload.ApiResponse;
import in.maisonnoir.backend.api.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@RequestBody @Valid OrderDTO dto) {
        OrderDTO created = orderService.createOrder(dto);
        return ResponseEntity.ok(new ApiResponse(true, "Order placed successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse(true, "Orders fetched", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Order fetched", order));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        OrderDTO updated = orderService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse(true, "Order status updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok(new ApiResponse(true, "Order cancelled", null));
    }
}
