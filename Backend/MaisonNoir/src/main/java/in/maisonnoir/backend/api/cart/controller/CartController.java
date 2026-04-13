package in.maisonnoir.backend.api.cart.controller;

import in.maisonnoir.backend.api.cart.model.dto.CartItemAddDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartItemUpdateDTO;
import in.maisonnoir.backend.api.cart.service.CartService;
import in.maisonnoir.backend.api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Endpoints for managing the shopping cart and cart items")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get user's cart", description = "Fetches the current user's cart with all items. Prices are refreshed from latest product data.")
    public ResponseEntity<ApiResponse> getCart() {
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Cart fetched successfully",
                        cartService.getUserCart())
        );
    }

    @PostMapping("/add")
    @Operation(summary = "Add item to cart", description = "Adds a product variant to the cart. If the same product+size already exists, quantity is incremented.")
    public ResponseEntity<ApiResponse> addItem(@Valid @RequestBody CartItemAddDTO cartItemAddDTO) {
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Item added to cart successfully",
                        cartService.addCartItem(cartItemAddDTO))
        );
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of a specific cart item")
    public ResponseEntity<ApiResponse> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemUpdateDTO cartItemUpdateDTO) {
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Cart item updated successfully",
                        cartService.updateCartItem(cartItemUpdateDTO, cartItemId)
                )
        );
    }

    @DeleteMapping("/remove/{cartItemId}")
    @Operation(summary = "Remove item from cart", description = "Removes a specific item from the cart")
    public ResponseEntity<ApiResponse> removeItem(
            @PathVariable Long cartItemId) {
        return ResponseEntity.ok(
                new ApiResponse(
                        true,
                        "Item removed from cart successfully",
                        cartService.removeCartItem(cartItemId))
        );
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart", description = "Removes all items from the cart and resets the total to zero")
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
