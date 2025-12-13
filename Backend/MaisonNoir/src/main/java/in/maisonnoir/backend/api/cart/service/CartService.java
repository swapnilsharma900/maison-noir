package in.maisonnoir.backend.api.cart.service;

import in.maisonnoir.backend.api.cart.model.dto.CartDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartDetailedDTO;

public interface CartService {
    CartDTO getCart(Long userId);
    CartDetailedDTO getDetailedCart(Long userId);
    CartDTO addItem(Long userId, String productId, int quantity);
    CartDTO removeItem(Long userId, String productId);
    void clearCart(Long userId);

}
