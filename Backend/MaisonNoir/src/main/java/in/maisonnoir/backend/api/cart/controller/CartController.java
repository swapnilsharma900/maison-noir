package in.maisonnoir.backend.api.cart.controller;

import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemAddDTO;
import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemUpdateDTO;
import in.maisonnoir.backend.api.cart.service.CartService;

import in.maisonnoir.backend.api.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse> getCart() {
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Cart fetched successfully",
                        cartService.getUserCart())
        );
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItem( @Valid @RequestBody CartItemAddDTO cartItemAddDTO) {
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Item added to cart successfully",
                        cartService.addCartItem(cartItemAddDTO))
        );
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse> updateCartItem(
            @PathVariable String itemId,
            @Valid @RequestBody CartItemUpdateDTO cartItemUpdateDTO) {
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Cart item updated successfully",
                        cartService.updateCartItem(cartItemUpdateDTO, itemId)
                )
        );
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<ApiResponse> removeItem(
            @PathVariable String itemId) {
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Item removed from cart successfully",
                        cartService.removeCartItem(itemId))
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Cart cleared successfully",
                        null)
        );
    }

}
