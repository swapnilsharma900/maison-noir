package in.maisonnoir.backend.api.account.service.impl;

import in.maisonnoir.backend.api.account.model.dto.user.UserRegistrationDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserResponseDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserUpdateDTO;
import in.maisonnoir.backend.api.account.model.enums.AccountRole;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.cart.service.impl.CartServiceImpl;
import in.maisonnoir.backend.api.common.exception.DuplicateResourceException;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.account.mapper.UserMapper;
import in.maisonnoir.backend.api.account.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final CartDAO cartDAO;
    private final PasswordEncoder passwordEncoder;
    private final CartServiceImpl cartService;

    private UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public UserResponseDTO createUser(UserRegistrationDTO userDTO) {
        if (userDAO.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException(
                    "User", "email", userDTO.getEmail(),
                    "A user already exists with this email");
        }

        UserEntity entity = UserMapper.toEntity(userDTO);

        // Hash password before saving
        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        System.out.println(
                "\n\nhashed password for admin123: " + new BCryptPasswordEncoder().encode("admin123") + "\n\n");

        // Default role to CUSTOMER
        entity.setRole(AccountRole.CUSTOMER);

        // Create empty cart for new user
        CartEntity cart = CartEntity.builder()
                .itemIds(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .build();
        cart = cartDAO.save(cart);
        entity.setCart(cart);

        UserEntity saved = userDAO.save(entity);
        return UserMapper.toResponse(saved);
    }

    @Override
    public UserResponseDTO getUser() {
        UserEntity currentUser = getCurrentUser();
        return UserMapper.toResponse(currentUser);
    }

    @Override
    public UserResponseDTO updateUser(UserUpdateDTO userDTO) {
        UserEntity currentUser = getCurrentUser();

        boolean changed;

        // Apply updates (excluding password)
        changed = UserMapper.applyUpdate(userDTO, currentUser);

        // If password provided, hash and set
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            currentUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            changed = true;
        }

        UserEntity updated = userDAO.save(currentUser);
        return (changed) ? UserMapper.toResponse(updated) : null;
    }

    @Override
    public void deleteUser() {
        UserEntity currentUser = getCurrentUser();

        // Delete cart first
        if (currentUser.getCart() != null) {
            cartService.deleteUserCart(currentUser.getCart().getCartId());
        }

        userDAO.delete(currentUser);
    }

    // admin only
    @Override
    public UserResponseDTO getUserById(Long userId) {
        UserEntity entity = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return UserMapper.toResponse(entity);
    }

    // admin only
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    // admin only
    @Override
    public void deleteUserById(Long userId) {
        UserEntity entity = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        userDAO.delete(entity);
    }
}
