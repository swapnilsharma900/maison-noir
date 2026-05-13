package in.maisonnoir.backend.api.address.service.impl;

import in.maisonnoir.backend.api.address.mapper.AddressMapper;
import in.maisonnoir.backend.api.address.model.dto.AddressDTO;
import in.maisonnoir.backend.api.address.model.entity.AddressEntity;
import in.maisonnoir.backend.api.user.model.entity.UserEntity;
import in.maisonnoir.backend.api.address.repository.AddressDAO;
import in.maisonnoir.backend.api.user.repository.UserDAO;
import in.maisonnoir.backend.api.address.service.AddressService;
import in.maisonnoir.backend.api.common.exception.DuplicateResourceException;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressDAO addressDAO;
    private final UserDAO userDAO;

    private Long getAuthenticatedUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userDAO.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return user.getId();
    }

    @Override
    public AddressDTO getUserAddress() {
        Long userId = getAuthenticatedUserId();

        AddressEntity address = addressDAO.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "userId", userId));

        return AddressMapper.toDTO(address);
    }

    @Override
    public AddressDTO addAddress(AddressDTO addressDTO) {
        Long userId = getAuthenticatedUserId();

        if (addressDAO.findByUserId(userId).isPresent()) {
            throw new DuplicateResourceException(
                    "Address",
                    "userId",
                    userId,
                    "Address already exists. Try updating instead."
            );
        }

        AddressEntity address = AddressMapper.toEntity(addressDTO);
        address.setUserId(userId);
        addressDAO.save(address);
        return AddressMapper.toDTO(address);
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO) {
        Long userId = getAuthenticatedUserId();

        AddressEntity address = addressDAO.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "userId", userId));

        address.setLineOne(addressDTO.getLineOne());
        address.setLineTwo(addressDTO.getLineTwo());
        address.setLandmark(addressDTO.getLandmark());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPincode(addressDTO.getPincode());
        address.setCountry(addressDTO.getCountry());

        addressDAO.save(address);
        return AddressMapper.toDTO(address);
    }
}
