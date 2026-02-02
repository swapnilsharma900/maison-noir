package in.maisonnoir.backend.api.account.controller;

import in.maisonnoir.backend.api.account.model.dto.user.UserRegistrationDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserResponseDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserUpdateDTO;
import in.maisonnoir.backend.api.common.response.ApiResponse;
import in.maisonnoir.backend.api.account.service.UserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(
            @Valid
            @RequestBody UserRegistrationDTO userDTO
    ) {
        UserResponseDTO user = userService.createUser(userDTO);
        return ResponseEntity.ok(
                new ApiResponse(true, "User Created Successfully", user)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getUser() {
        UserResponseDTO user = userService.getUser(); // service resolves from context
        System.out.println("\n\n\n user: "+user+"\n\n\n");
        return ResponseEntity.ok(
                new ApiResponse(true, "Fetched current user", user)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateUser(
            @Valid @RequestBody UserUpdateDTO userDTO) {
        UserResponseDTO user = userService.updateUser(userDTO);
        return (user != null) ?
                ResponseEntity.ok(
                        new ApiResponse(true, "User Updated Successfully", user )
                ) : ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(
                        new ApiResponse(false, "No Changes Applied", null)
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok(
                new ApiResponse(true, "User deleted successfully", null)
        );
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Fetched user with userId: "+userId, user)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponse(true, "Fetched all users successfully", users)
        );
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok(
                new ApiResponse(true, "User deleted successfully with userId: "+userId, null)
        );
    }
}
