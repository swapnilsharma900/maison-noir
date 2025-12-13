package in.maisonnoir.backend.api.cart.controller;

import in.maisonnoir.backend.api.cart.model.dto.CartDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartDetailedDTO;
import in.maisonnoir.backend.api.cart.service.CartService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItem(
            @PathVariable Long userId,
            @RequestParam String productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addItem(userId, productId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartDTO> removeItem(
            @PathVariable Long userId,
            @RequestParam String productId) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detailed")
    public ResponseEntity<CartDetailedDTO> getDetailedCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getDetailedCart(userId));
    }

}
