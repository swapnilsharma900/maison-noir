package in.maisonnoir.backend.api.account.service.impl;

import in.maisonnoir.backend.api.account.model.dto.user.UpdatePasswordDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserResponseDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserUpdateDTO;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.cart.repository.CartItemDAO;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.account.mapper.UserMapper;
import in.maisonnoir.backend.api.account.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final CartDAO cartDAO;
    private final CartItemDAO cartItemDAO;
    private final PasswordEncoder passwordEncoder;

    private UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public UserResponseDTO getUser() {
        UserEntity currentUser = getCurrentUser();
        return UserMapper.toResponse(currentUser);
    }

    @Override
    public UserResponseDTO updateUser(UserUpdateDTO userDTO) {
        UserEntity currentUser = getCurrentUser();

        boolean changed = UserMapper.applyUpdate(userDTO, currentUser);

        UserEntity updated = userDAO.save(currentUser);
        return (changed) ? UserMapper.toResponse(updated) : null;
    }

    @Override
    public void updatePassword(UpdatePasswordDTO dto) {
        UserEntity currentUser = getCurrentUser();

        // Verify old password
        if (!passwordEncoder.matches(dto.getOldPassword(), currentUser.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Validate new passwords match
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // Ensure new password is different from old
        if (passwordEncoder.matches(dto.getNewPassword(), currentUser.getPassword())) {
            throw new RuntimeException("New password must be different from current password");
        }

        currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userDAO.save(currentUser);
    }

    @Override
    public void deleteUser() {
        UserEntity currentUser = getCurrentUser();

        // Delete cart and cart items
        cartDAO.findByUserId(currentUser.getId()).ifPresent(cart -> {
            cartItemDAO.deleteByCartId(cart.getId());
            cartDAO.delete(cart);
        });

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
