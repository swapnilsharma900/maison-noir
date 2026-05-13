package in.maisonnoir.backend.api.user.controller;

import in.maisonnoir.backend.api.user.model.dto.UpdatePasswordDTO;
import in.maisonnoir.backend.api.user.model.dto.UserResponseDTO;
import in.maisonnoir.backend.api.user.model.dto.UserUpdateDTO;
import in.maisonnoir.backend.api.common.response.ApiResponse;
import in.maisonnoir.backend.api.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing user profile, password, and admin user operations")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Returns the profile details of the currently authenticated user")
    public ResponseEntity<ApiResponse> getUser() {
        UserResponseDTO user = userService.getUser();
        return ResponseEntity.ok(
                new ApiResponse(true, "Fetched current user", user)
        );
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Updates profile fields (firstName, lastName, email, phone) for the authenticated user")
    public ResponseEntity<ApiResponse> updateUser(
            @Valid @RequestBody UserUpdateDTO userDTO) {
        UserResponseDTO user = userService.updateUser(userDTO);
        return (user != null) ?
                ResponseEntity.ok(
                        new ApiResponse(true, "User Updated Successfully", user)
                ) : ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                new ApiResponse(false, "No Changes Applied", null)
        );
    }

    @PutMapping("/me/password")
    @Operation(summary = "Update password", description = "Changes the current user's password. Requires old password for verification.")
    public ResponseEntity<ApiResponse> updatePassword(
            @Valid @RequestBody UpdatePasswordDTO dto) {
        userService.updatePassword(dto);
        return ResponseEntity.ok(
                new ApiResponse(true, "Password updated successfully", null)
        );
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete current user account", description = "Permanently deletes the authenticated user account and associated cart")
    public ResponseEntity<ApiResponse> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok(
                new ApiResponse(true, "User deleted successfully", null)
        );
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID (Admin)", description = "Fetches a specific user's profile by their ID. Admin access only.")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Fetched user with userId: " + userId, user)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin)", description = "Fetches the list of all registered users. Admin access only.")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponse(true, "Fetched all users successfully", users)
        );
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user by ID (Admin)", description = "Permanently deletes a user by their ID. Admin access only.")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok(
                new ApiResponse(true, "User deleted successfully with userId: " + userId, null)
        );
    }
}
