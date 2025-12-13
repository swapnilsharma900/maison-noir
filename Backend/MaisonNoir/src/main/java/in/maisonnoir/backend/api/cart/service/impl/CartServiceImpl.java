package in.maisonnoir.backend.api.cart.service.impl;

import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.cart.mapper.CartMapper;
import in.maisonnoir.backend.api.cart.model.dto.CartDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartDetailedDTO;
import in.maisonnoir.backend.api.cart.model.dto.CartItemDetailedDTO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartItemEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.cart.repository.CartItemDAO;
import in.maisonnoir.backend.api.cart.service.CartService;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import in.maisonnoir.backend.api.product.repository.ProductDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final CartDAO cartDAO;
    private final CartItemDAO cartItemDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;
    private final CartMapper cartMapper;

    @Override
    public CartDTO getCart(Long userId) {
        CartEntity cart = cartDAO.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        return cartMapper.toDTO(cart);
    }

    @Override
    public CartDTO addItem(Long userId, String productId, int quantity) {

        CartEntity cart = cartDAO.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        // Validate product exists in MongoDB
        productDAO.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item already exists
        CartItemEntity existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .cart(cart)
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
        }

        return cartMapper.toDTO(cartDAO.save(cart));
    }

    @Override
    public CartDTO removeItem(Long userId, String productId) {
        CartEntity cart = cartDAO.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        return cartMapper.toDTO(cartDAO.save(cart));
    }

    @Override
    public void clearCart(Long userId) {
        CartEntity cart = cartDAO.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cartDAO.save(cart);
    }
    private CartEntity createCart(Long userId) {
        UserEntity user = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartEntity cart = CartEntity.builder()
                .user(user)
                .items(new ArrayList<>())
                .build();

        return cartDAO.save(cart);
    }

    @Override
    public CartDetailedDTO getDetailedCart(Long userId) {

        CartEntity cart = cartDAO.findByUserId(userId)
                .orElseGet(() -> createCart(userId));

        List<CartItemDetailedDTO> detailedItems = cart.getItems().stream()
                .map(item -> {
                    ProductEntity product = productDAO.findById(item.getProductId())
                            .orElse(null);

                    double subtotal = 0;
                    if (product != null && product.getPrice() != null) {
                        subtotal = product.getPrice() * item.getQuantity();
                    }

                    return CartItemDetailedDTO.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .product(product)
                            .subtotal(subtotal)
                            .build();
                })
                .toList();
        double totalAmount = detailedItems.stream()
                .mapToDouble(CartItemDetailedDTO::getSubtotal)
                .sum();

        int totalItems = detailedItems.stream()
                .mapToInt(CartItemDetailedDTO::getQuantity)
                .sum();

        return CartDetailedDTO.builder()
                .userId(userId)
                .items(detailedItems)
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .build();
    }
}
