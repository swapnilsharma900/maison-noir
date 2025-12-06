package in.maisonnoir.backend.api.account.service;

import in.maisonnoir.backend.api.account.model.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getUserAddresses(Long userId);
    AddressDTO addAddress(Long userId, AddressDTO addressDTO);
    public AddressDTO setDefaultAddress(Long userId, Long addressId);
    void deleteAddress(Long userId, Long addressId);
}
