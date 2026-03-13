package in.maisonnoir.backend.api.cart.service.impl;

import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.cart.mapper.CartMapper;
import in.maisonnoir.backend.api.common.item.mapper.CartItemMapper;
import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemAddDTO;
import in.maisonnoir.backend.api.common.item.model.dto.cartItem.CartItemUpdateDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartResponseDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.cart.service.CartService;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.common.item.model.entity.ItemEntity;
import in.maisonnoir.backend.api.common.item.repository.ItemDAO;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import in.maisonnoir.backend.api.product.repository.ProductDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartDAO cartDAO;
    private final ItemDAO itemDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    private CartEntity getAuthenticatedUserCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return user.getCart();
    }

    /**
     * Refresh product snapshots and prices from the master product collection.
     * This ensures that cart items always reflect the latest product pricing.
     */
    private List<ItemEntity> refreshCartItemPrices(List<ItemEntity> cartItems) {
        boolean updated = false;
        for (ItemEntity item : cartItems) {
            productDAO.findById(item.getProductId()).ifPresent(latestProduct -> {
                item.setProductSnapshot(latestProduct);
            });
        }
        // Batch save all refreshed items
        itemDAO.saveAll(cartItems);
        return cartItems;
    }

    private void updateCartTotals(CartEntity cart, List<ItemEntity> cartItems) {
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getProductSnapshot().getProductPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cart.setUpdatedAt(LocalDateTime.now());
        cartDAO.save(cart);
        log.info("Cart updated with {} items, total: {}", cartItems.size(), totalAmount);
    }

    private CartEntity createCart(UserEntity currentUser) {
        CartEntity cart = new CartEntity();
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setItemIds(new ArrayList<>());
        cart = cartDAO.save(cart);

        currentUser.setCart(cart);
        userDAO.save(currentUser);

        log.info("New cart created for user itemId: {} with cart itemId: {}", currentUser.getUserId(),
                cart.getCartId());
        return cart;
    }

    @Override
    public CartResponseDTO getUserCart() {
        CartEntity cart = getAuthenticatedUserCart();
        if (cart == null) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity currentUser = userDAO.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

            cart = createCart(currentUser);
        }

        // Fetch items and refresh prices from the master product
        List<ItemEntity> cartItems = itemDAO.findByCartId(cart.getCartId());
        cartItems = refreshCartItemPrices(cartItems);

        // Recalculate totals with live prices
        updateCartTotals(cart, cartItems);

        log.info("Cart fetched with itemId: {}", cart.getCartId());
        return CartMapper.toResponse(cart, cartItems);
    }

    @Override
    public void deleteUserCart(Long cartId) {
        // Delete all cart items from MongoDB
        itemDAO.deleteByCartId(cartId);

        // Delete cart from MySQL
        cartDAO.deleteById(cartId);
        log.info("Cart Deleted Successfully");
    }

    @Override
    public void clearCart() {
        CartEntity cart = getAuthenticatedUserCart();
        if (cart == null) {
            throw new RuntimeException("Cart not found for user");
        }

        // Delete all cart items from MongoDB
        itemDAO.deleteByCartId(cart.getCartId());
        // Clear cart item IDs and reset totals
        cart.getItemIds().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartDAO.save(cart);
        log.info("Cart cleared with itemId: {}", cart.getCartId());
    }

    @Override
    public CartResponseDTO addCartItem(CartItemAddDTO dto) {
        CartEntity cart = getAuthenticatedUserCart();

        if (cart == null) {
            throw new RuntimeException("Cart not found for user");
        }

        // Fetch product
        ProductEntity product = productDAO.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", dto.getProductId()));

        // Check if item already exists in cart
        List<ItemEntity> itemList = itemDAO.findByCartId(cart.getCartId());
        ItemEntity existingItem = itemList.stream()
                .filter(item -> item.getProductId().equals(dto.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity of existing item
            existingItem.setQuantity(existingItem.getQuantity() + dto.getQuantity());
            existingItem.setProductSnapshot(product); // refresh snapshot
            itemDAO.save(existingItem);
        } else {
            // Create new cart item
            ItemEntity newItem = CartItemMapper.toEntity(dto, cart.getCartId(), product);
            newItem = itemDAO.save(newItem);
            cart.getItemIds().add(newItem.getItemId());
        }

        // Update cart totals
        List<ItemEntity> updatedItems = itemDAO.findByCartId(cart.getCartId());
        updateCartTotals(cart, updatedItems);

        log.info("Cart item: {} added successfully to the cart with itemId: {}", dto, cart.getCartId());
        return CartMapper.toResponse(cart, updatedItems);
    }

    @Override
    public CartResponseDTO updateCartItem(CartItemUpdateDTO dto, String itemId) {
        CartEntity cart = getAuthenticatedUserCart();
        if (cart == null) {
            throw new RuntimeException("Cart not found for user");
        }

        // Find cart item
        ItemEntity cartItem = itemDAO.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        // Verify item belongs to current user's cart
        if (!cartItem.getCartId().equals(cart.getCartId())) {
            throw new ResourceNotFoundException("Item", "id", itemId);
        }

        // Apply update
        CartItemMapper.applyUpdate(cartItem, dto);
        itemDAO.save(cartItem);

        // Update cart totals
        List<ItemEntity> cartItems = itemDAO.findByCartId(cart.getCartId());
        updateCartTotals(cart, cartItems);

        log.info("Cart item: {} with item itemId: {} updated successfully to the cart with itemId: {}",
                dto, itemId, cart.getCartId());
        return CartMapper.toResponse(cart, cartItems);
    }

    @Override
    public CartResponseDTO removeCartItem(String itemId) {
        CartEntity cart = getAuthenticatedUserCart();
        if (cart == null) {
            throw new RuntimeException("Cart not found for user");
        }

        // Find cart item
        ItemEntity cartItem = itemDAO.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        // Verify item belongs to current user's cart
        if (!cartItem.getCartId().equals(cart.getCartId())) {
            throw new ResourceNotFoundException("Item", "id", itemId);
        }
        // Remove item
        itemDAO.deleteById(itemId);
        cart.getItemIds().remove(itemId);

        // Update cart totals
        List<ItemEntity> cartItems = itemDAO.findByCartId(cart.getCartId());
        updateCartTotals(cart, cartItems);

        log.info("Cart Item with itemId: {} Deleted Successfully", itemId);
        return CartMapper.toResponse(cart, cartItems);
    }

}
