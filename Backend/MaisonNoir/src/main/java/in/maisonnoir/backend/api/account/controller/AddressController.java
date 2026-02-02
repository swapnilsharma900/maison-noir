package in.maisonnoir.backend.api.account.controller;

import in.maisonnoir.backend.api.account.model.dto.address.AddressDTO;
import in.maisonnoir.backend.api.account.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<AddressDTO> getUserAddress() {
        return ResponseEntity.ok(addressService.getUserAddress());
    }

    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(
            @Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.addAddress(addressDTO));
    }

    @PutMapping
    public ResponseEntity<AddressDTO> updateAddress(
            @Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.updateAddress(addressDTO));
    }
}
