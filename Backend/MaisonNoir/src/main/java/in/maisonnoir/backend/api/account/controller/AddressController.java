package in.maisonnoir.backend.api.account.controller;

import in.maisonnoir.backend.api.account.model.dto.AddressDTO;
import in.maisonnoir.backend.api.account.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(
            @PathVariable Long userId,
            @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.addAddress(userId, addressDTO));
    }

    @PutMapping("/{addressId}/default")
    public ResponseEntity<AddressDTO> setDefaultAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.setDefaultAddress(userId, addressId));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }

}
