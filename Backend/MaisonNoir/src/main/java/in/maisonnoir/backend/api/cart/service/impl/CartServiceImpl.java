package in.maisonnoir.backend.api.cart.service.impl;

import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.cart.mapper.CartItemMapper;
import in.maisonnoir.backend.api.cart.mapper.CartMapper;
import in.maisonnoir.backend.api.cart.model.dto.CartItemAddDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartItemUpdateDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartResponseDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.cart.repository.CartItemDAO;
import in.maisonnoir.backend.api.cart.service.CartService;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.product.model.entity.ProductVariantEntity;
import in.maisonnoir.backend.api.product.repository.ProductVariantDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartDAO cartDAO;
    private final CartItemDAO cartItemDAO;
    private final ProductVariantDAO variantDAO;
    private final UserDAO userDAO;

    private UserEntity getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private CartEntity getOrCreateCart(UserEntity user) {
        return cartDAO.findByUserId(user.getId())
                .orElseGet(() -> {
                    CartEntity cart = CartEntity.builder()
                            .userId(user.getId())
                            .totalAmount(BigDecimal.ZERO)
                            .build();
                    cart = cartDAO.save(cart);
                    log.info("New cart created for user id: {} with cart id: {}", user.getId(), cart.getId());
                    return cart;
                });
    }

    /**
     * Refresh product snapshots and prices from the master variant catalog.
     */
    private void refreshCartItemPrices(List<CartItemEntity> cartItems) {
        for (CartItemEntity cartItem : cartItems) {
            variantDAO.findById(cartItem.getVariantId()).ifPresent(variant -> {
                CartItemMapper.refreshSnapshot(cartItem, variant);
            });
        }
        cartItemDAO.saveAll(cartItems);
    }

    private void updateCartTotals(CartEntity cart, List<CartItemEntity> cartItems) {
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getSnapshotPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartDAO.save(cart);
        log.info("Cart updated with {} items, total: {}", cartItems.size(), totalAmount);
    }

    @Override
    public CartResponseDTO getUserCart() {
        UserEntity user = getAuthenticatedUser();
        CartEntity cart = getOrCreateCart(user);

        List<CartItemEntity> cartItems = cartItemDAO.findByCartId(cart.getId());
        refreshCartItemPrices(cartItems);
        updateCartTotals(cart, cartItems);

        log.info("Cart fetched with id: {}", cart.getId());
        return CartMapper.toResponse(cart, cartItems);
    }

    @Override
    public void deleteUserCart(Long userId) {
        cartDAO.findByUserId(userId).ifPresent(cart -> {
            cartItemDAO.deleteByCartId(cart.getId());
            cartDAO.delete(cart);
            log.info("Cart deleted for user id: {}", userId);
        });
    }

    @Override
    public void clearCart() {
        UserEntity user = getAuthenticatedUser();
        CartEntity cart = cartDAO.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getId()));

        cartItemDAO.deleteByCartId(cart.getId());
        cart.setTotalAmount(BigDecimal.ZERO);
        cartDAO.save(cart);
        log.info("Cart cleared with id: {}", cart.getId());
    }

    @Override
    public CartResponseDTO addCartItem(CartItemAddDTO dto) {
        UserEntity user = getAuthenticatedUser();
        CartEntity cart = getOrCreateCart(user);

        // Fetch the variant (SKU) from MongoDB
        ProductVariantEntity variant = variantDAO.findById(dto.getVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "id", dto.getVariantId()));

        // Check if the same variant already exists in cart
        List<CartItemEntity> existingItems = cartItemDAO.findByCartId(cart.getId());
        CartItemEntity existingItem = existingItems.stream()
                .filter(ci -> ci.getVariantId().equals(dto.getVariantId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity of existing item
            existingItem.setQuantity(existingItem.getQuantity() + dto.getQuantity());
            cartItemDAO.save(existingItem);
        } else {
            // Create new SQL cart item directly from the variant snapshot
            CartItemEntity cartItem = CartItemMapper.toEntity(cart, variant, dto.getQuantity());
            cartItemDAO.save(cartItem);
        }

        // Update cart totals
        List<CartItemEntity> updatedItems = cartItemDAO.findByCartId(cart.getId());
        updateCartTotals(cart, updatedItems);

        log.info("Cart item added to cart id: {}", cart.getId());
        return CartMapper.toResponse(cart, updatedItems);
    }

    @Override
    public CartResponseDTO updateCartItem(CartItemUpdateDTO dto, Long cartItemId) {
        UserEntity user = getAuthenticatedUser();
        CartEntity cart = cartDAO.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getId()));

        CartItemEntity cartItem = cartItemDAO.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        // Verify item belongs to current user's cart
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("CartItem", "id", cartItemId);
        }

        cartItem.setQuantity(dto.getQuantity());
        cartItemDAO.save(cartItem);

        List<CartItemEntity> cartItems = cartItemDAO.findByCartId(cart.getId());
        updateCartTotals(cart, cartItems);

        log.info("Cart item id: {} updated in cart id: {}", cartItemId, cart.getId());
        return CartMapper.toResponse(cart, cartItems);
    }

    @Override
    public CartResponseDTO removeCartItem(Long cartItemId) {
        UserEntity user = getAuthenticatedUser();
        CartEntity cart = cartDAO.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getId()));

        CartItemEntity cartItem = cartItemDAO.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        // Verify item belongs to current user's cart
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new ResourceNotFoundException("CartItem", "id", cartItemId);
        }

        // Delete only from SQL — no MongoDB writes needed
        cartItemDAO.delete(cartItem);

        List<CartItemEntity> cartItems = cartItemDAO.findByCartId(cart.getId());
        updateCartTotals(cart, cartItems);

        log.info("Cart item id: {} removed from cart id: {}", cartItemId, cart.getId());
        return CartMapper.toResponse(cart, cartItems);
    }
}
