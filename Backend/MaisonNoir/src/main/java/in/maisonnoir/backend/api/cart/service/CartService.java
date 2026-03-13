package in.maisonnoir.backend.api.cart.service;

import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemAddDTO;
import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemUpdateDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartResponseDTO;

public interface CartService {
    // for cart
    CartResponseDTO getUserCart();
    void deleteUserCart(Long cartId); // should only be called by delete user method
    void clearCart();

    // for cart item
    CartResponseDTO addCartItem(CartItemAddDTO cartItemAddDTO);
    CartResponseDTO updateCartItem(CartItemUpdateDTO cartItemUpdateDTO, String itemId);
    CartResponseDTO removeCartItem(String itemId);
}
