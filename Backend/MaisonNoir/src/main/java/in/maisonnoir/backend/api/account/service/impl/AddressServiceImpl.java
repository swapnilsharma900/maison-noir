package in.maisonnoir.backend.api.account.service.impl;

import in.maisonnoir.backend.api.account.mapper.AddressMapper;
import in.maisonnoir.backend.api.account.model.dto.address.AddressDTO;
import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.account.repository.AddressDAO;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.account.service.AddressService;
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
        return user.getUserId();
    }

    @Override
    public AddressDTO getUserAddress() {
        Long userId = getAuthenticatedUserId();
        UserEntity user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        AddressEntity address = user.getAddress();
        if (address == null) {
            throw new ResourceNotFoundException("Address", "id", userId);
        }

        return AddressMapper.toDTO(address);
    }

    @Override
    public AddressDTO addAddress(AddressDTO addressDTO) {
        Long userId = getAuthenticatedUserId();
        UserEntity user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (user.getAddress() != null) {
            throw new DuplicateResourceException(
                    "Address",
                    "user id",
                    userId,
                    "Try updating address with new address"
            );
        }

        AddressEntity address = AddressMapper.toEntity(addressDTO);
        addressDAO.save(address);
        user.setAddress(address);
        userDAO.save(user);
        return AddressMapper.toDTO(address);
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO) {
        Long userId = getAuthenticatedUserId();
        UserEntity user = userDAO.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        AddressEntity address = user.getAddress();

        if (address == null) {
            throw new RuntimeException("No address found for this user");
        }

        if(isShared(address)){
            AddressEntity newAddress = AddressMapper.toEntity(addressDTO);
            addressDAO.save(newAddress);
            user.setAddress(newAddress);
            userDAO.save(user);
            return AddressMapper.toDTO(newAddress);
        }

        // if address is not shared
        address.setMainLine(addressDTO.getMainLine());
        address.setLocality(addressDTO.getLocality());
        address.setLandmark(addressDTO.getLandmark());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountry(addressDTO.getCountry());

        addressDAO.save(address);
        return AddressMapper.toDTO(address);

    }

    private boolean isShared(AddressEntity address) {
        return userDAO.countByAddress(address) > 1;
    }

}
