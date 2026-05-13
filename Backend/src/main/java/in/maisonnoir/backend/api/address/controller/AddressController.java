package in.maisonnoir.backend.api.address.controller;

import in.maisonnoir.backend.api.address.model.dto.AddressDTO;
import in.maisonnoir.backend.api.address.service.AddressService;
import in.maisonnoir.backend.api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
@Tag(name = "Address Management", description = "Endpoints for managing the user's shipping address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    @Operation(summary = "Get user address", description = "Fetches the address of the currently authenticated user")
    public ResponseEntity<ApiResponse> getUserAddress() {
        return ResponseEntity.ok(
                new ApiResponse(true, "Address fetched successfully", addressService.getUserAddress())
        );
    }

    @PostMapping
    @Operation(summary = "Add address", description = "Creates a new address for the authenticated user. Only one address per user.")
    public ResponseEntity<ApiResponse> addAddress(
            @Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(
                new ApiResponse(true, "Address added successfully", addressService.addAddress(addressDTO))
        );
    }

    @PutMapping
    @Operation(summary = "Update address", description = "Updates the existing address of the authenticated user")
    public ResponseEntity<ApiResponse> updateAddress(
            @Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(
                new ApiResponse(true, "Address updated successfully", addressService.updateAddress(addressDTO))
        );
    }
}
