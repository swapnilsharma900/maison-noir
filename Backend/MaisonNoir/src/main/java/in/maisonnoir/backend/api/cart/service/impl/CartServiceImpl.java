package in.maisonnoir.backend.api.cart.service.impl;

import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.cart.mapper.CartMapper;
import in.maisonnoir.backend.api.cart.model.dto.cartItem.CartItemAddDTO;
import in.maisonnoir.backend.api.cart.model.dto.cartItem.CartItemUpdateDTO;
import in.maisonnoir.backend.api.cart.model.dto.cart.CartResponseDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.cart.repository.CartItemDAO;
import in.maisonnoir.backend.api.cart.service.CartService;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import in.maisonnoir.backend.api.product.repository.ProductDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    private final CartItemDAO cartItemDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    private CartEntity getAuthenticatedUserCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return user.getCart();
    }

    private void updateCartTotals(CartEntity cart, List<CartItemEntity> cartItems) {
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartDAO.save(cart);
        log.info("Cart updated with item: {}", cartItems);
    }

    private CartEntity createCart(UserEntity currentUser) {
        // Create a new cart for this user
        CartEntity cart = new CartEntity();
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setCartItemIds(new ArrayList<>());
        cart = cartDAO.save(cart);

        currentUser.setCart(cart);
        userDAO.save(currentUser);

        log.info("New cart created for user id: {} with cart id: {}", currentUser.getUserId(), cart.getCartId());
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
        List<CartItemEntity> cartItems = cartItemDAO.findByCartId(cart.getCartId());
        log.info("Cart fetched with id: {}", cart.getCartId());
        return CartMapper.toResponse(cart, cartItems);
    }

    @Override
    public void deleteUserCart(Long cartId) {
        // Delete all cart items from MongoDB
        cartItemDAO.deleteByCartId(cartId);

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
        cartItemDAO.deleteByCartId(cart.getCartId());
        // Clear cart item IDs and reset totals
        cart.getCartItemIds().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartDAO.save(cart);
        log.info("Cart cleared with id: {}", cart.getCartId());
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


        // check if item already exist in cart
        // if item is already present, increment the quantity
        List<CartItemEntity> itemList = cartItemDAO.findByCartId(cart.getCartId());
        CartItemEntity existingItem = itemList.stream()
                .filter(item -> item.getProduct().getId().equals(dto.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity of existing item
            existingItem.setQuantity(existingItem.getQuantity() + dto.getQuantity());
            cartItemDAO.save(existingItem);
        } else {
            // Create new cart item
            CartItemEntity newItem = CartMapper.toItemEntity(dto, cart.getCartId(), product);
            newItem = cartItemDAO.save(newItem);
            cart.getCartItemIds().add(newItem.getItemId());
        }

        // Update cart totals
        List<CartItemEntity> updatedItems = cartItemDAO.findByCartId(cart.getCartId());
        updateCartTotals(cart, updatedItems);

        log.info("Cart item: {} added successfully to the cart with id: {}", dto, cart.getCartId());
        return CartMapper.toResponse(cart, updatedItems);
    }

    @Override
    public CartResponseDTO updateCartItem(CartItemUpdateDTO dto, String itemId) {
        CartEntity cart = getAuthenticatedUserCart();
        if (cart == null) {
            throw new RuntimeException("Cart not found for user");
        }

        // Find cart item
        CartItemEntity cartItem = cartItemDAO.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

        // Verify item belongs to current user's cart
        if (!cartItem.getCartId().equals(cart.getCartId())) {
            throw new ResourceNotFoundException("CartItem", "id", itemId);
        }

        // Update cart totals
        List<CartItemEntity> cartItems = cartItemDAO.findByCartId(cart.getCartId());
        updateCartTotals(cart, cartItems);

        log.info("Cart item: {} with item id: {} updated successfully to the cart with id: {}",
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
        CartItemEntity cartItem = cartItemDAO.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));
        System.out.println("\n\n\n cart:"+cartItem);

        // Verify item belongs to current user's cart
        if (!cartItem.getCartId().equals(cart.getCartId())) {
            throw new ResourceNotFoundException("CartItem", "id", itemId);
        }
        // Remove item
        cartItemDAO.deleteById(itemId);
        cart.getCartItemIds().remove(itemId);

        // Update cart totals
        List<CartItemEntity> cartItems = cartItemDAO.findByCartId(cart.getCartId());
        updateCartTotals(cart, cartItems);

        log.info("Cart Item with id: {} Deleted Successfully", itemId);
        return CartMapper.toResponse(cart, cartItems);
    }

}
