package in.maisonnoir.backend.api.cart.service;

import in.maisonnoir.backend.api.cart.model.dto.CartItemAddDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartItemUpdateDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartResponseDTO;

public interface CartService {
    // for cart
    CartResponseDTO getUserCart();
    void deleteUserCart(Long userId);
    void clearCart();

    // for cart item
    CartResponseDTO addCartItem(CartItemAddDTO cartItemAddDTO);
    CartResponseDTO updateCartItem(CartItemUpdateDTO cartItemUpdateDTO, Long cartItemId);
    CartResponseDTO removeCartItem(Long cartItemId);
}
