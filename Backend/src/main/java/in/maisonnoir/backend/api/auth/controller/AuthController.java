package in.maisonnoir.backend.api.auth.controller;

import in.maisonnoir.backend.api.auth.model.dto.AuthResponseDTO;
import in.maisonnoir.backend.api.auth.model.dto.LoginDTO;
import in.maisonnoir.backend.api.auth.model.dto.RegisterDTO;
import in.maisonnoir.backend.api.auth.service.AuthService;
import in.maisonnoir.backend.api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new customer account with an empty cart. No authentication required.")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterDTO registerDTO) {
        AuthResponseDTO response = authService.register(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates user with email and password. Returns user details on success.")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginDTO loginDTO) {
        AuthResponseDTO response = authService.login(loginDTO);
        return ResponseEntity.ok(
                new ApiResponse(true, "Login successful", response));
    }
}
