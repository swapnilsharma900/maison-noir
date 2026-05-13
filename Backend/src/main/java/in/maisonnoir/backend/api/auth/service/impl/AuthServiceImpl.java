package in.maisonnoir.backend.api.auth.service.impl;

import in.maisonnoir.backend.api.auth.model.dto.AuthResponseDTO;
import in.maisonnoir.backend.api.auth.model.dto.LoginDTO;
import in.maisonnoir.backend.api.auth.model.dto.RegisterDTO;
import in.maisonnoir.backend.api.auth.service.AuthService;
import in.maisonnoir.backend.api.user.model.entity.UserEntity;
import in.maisonnoir.backend.api.user.model.enums.AccountRole;
import in.maisonnoir.backend.api.user.repository.UserDAO;
import in.maisonnoir.backend.api.cart.model.entity.CartEntity;
import in.maisonnoir.backend.api.cart.repository.CartDAO;
import in.maisonnoir.backend.api.common.exception.BadRequestException;
import in.maisonnoir.backend.api.common.exception.DuplicateResourceException;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.config.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO;
    private final CartDAO cartDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponseDTO register(RegisterDTO dto) {
        // Validate passwords match
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password and confirmation do not match. Please re-enter.");
        }

        // Check duplicate email
        if (userDAO.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException(
                    "User", "email", dto.getEmail(),
                    "A user already exists with this email");
        }

        // Create user entity
        UserEntity user = UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(AccountRole.CUSTOMER)
                .build();
        user = userDAO.save(user);

        // Create empty cart for user
        CartEntity cart = CartEntity.builder()
                .userId(user.getId())
                .totalAmount(BigDecimal.ZERO)
                .build();
        cartDAO.save(cart);

        log.info("User registered successfully: {} {}", user.getFirstName(), user.getEmail());

        // Generate JWT token for the newly registered user
        String token = generateTokenForUser(user);

        return buildAuthResponse(user, token, "User registered successfully");
    }

    @Override
    public AuthResponseDTO login(LoginDTO dto) {
        // Authenticate using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Fetch user details
        UserEntity user = userDAO.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", dto.getEmail()));

        log.info("User logged in: {}", user.getEmail());

        // Generate JWT token
        String token = generateTokenForUser(user);

        return buildAuthResponse(user, token, "Login successful");
    }

    /**
     * Builds a UserDetails object from the entity and generates a JWT
     * with the user's role embedded as a custom claim.
     */
    private String generateTokenForUser(UserEntity user) {
        UserDetails userDetails = User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("userId", user.getId());

        return jwtService.generateToken(extraClaims, userDetails);
    }

    private AuthResponseDTO buildAuthResponse(UserEntity user, String token, String message) {
        return AuthResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().toString())
                .token(token)
                .message(message)
                .build();
    }
}
