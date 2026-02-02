package in.maisonnoir.backend.api.account.service;

import in.maisonnoir.backend.api.account.model.dto.address.AddressDTO;


public interface AddressService {
    AddressDTO getUserAddress();
    AddressDTO addAddress(AddressDTO addressDTO);
    AddressDTO updateAddress(AddressDTO addressDTO);
}
