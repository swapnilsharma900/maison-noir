package in.maisonnoir.backend.api.account.service.impl;

import in.maisonnoir.backend.api.account.mapper.AddressMapper;
import in.maisonnoir.backend.api.account.model.dto.AddressDTO;
import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import in.maisonnoir.backend.api.account.repository.AddressDAO;
import in.maisonnoir.backend.api.account.repository.UserDAO;
import in.maisonnoir.backend.api.account.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressDAO addressDAO;
    private final UserDAO userDAO;

    @Override
    public List<AddressDTO> getUserAddresses(Long userId) {
        return addressDAO.findByUserId(userId)
                .stream()
                .map(AddressMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO addAddress(Long userId, AddressDTO addressDTO) {
        UserEntity user = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AddressEntity entity = AddressMapper.toEntity(addressDTO);
        entity.setUser(user);

        // If this address is marked as default, reset others
        if (entity.isDefault()) {
            List<AddressEntity> existingAddresses = addressDAO.findByUserId(userId);
            existingAddresses.forEach(addr -> addr.setDefault(false));
            addressDAO.saveAll(existingAddresses);
        }

        return AddressMapper.toDTO(addressDAO.save(entity));
    }

    @Override
    public AddressDTO setDefaultAddress(Long userId, Long addressId) {
        List<AddressEntity> addresses = addressDAO.findByUserId(userId);

        addresses.forEach(addr -> addr.setDefault(addr.getId().equals(addressId)));
        addressDAO.saveAll(addresses);

        return AddressMapper.toDTO(
                addresses.stream()
                        .filter(AddressEntity::isDefault)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Address not found"))
        );
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        AddressEntity entity = addressDAO.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!entity.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized delete attempt");
        }

        addressDAO.delete(entity);
    }

}
