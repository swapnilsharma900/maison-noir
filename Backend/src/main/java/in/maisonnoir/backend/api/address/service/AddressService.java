package in.maisonnoir.backend.api.address.service;

import in.maisonnoir.backend.api.address.model.dto.AddressDTO;


public interface AddressService {
    AddressDTO getUserAddress();
    AddressDTO addAddress(AddressDTO addressDTO);
    AddressDTO updateAddress(AddressDTO addressDTO);
}
