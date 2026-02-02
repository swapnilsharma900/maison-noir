package in.maisonnoir.backend.api.account.mapper;

import in.maisonnoir.backend.api.account.model.dto.user.UserRegistrationDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserResponseDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserUpdateDTO;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(UserRegistrationDTO dto) {
        return UserEntity.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .address(dto.getAddress() == null ? null : AddressMapper.toEntity(dto.getAddress()))
                .build();
    }

    public static boolean applyUpdate(UserUpdateDTO dto, UserEntity entity) {
        boolean changed = false;

        if (dto.getUserName() != null && !dto.getUserName().isBlank()) {
            entity.setUserName(dto.getUserName());
            changed = true;
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            entity.setEmail(dto.getEmail());
            changed = true;
        }
        if (dto.getAddress() != null) {
            entity.setAddress(AddressMapper.toEntity(dto.getAddress()));
            changed = true;
        }
        return changed;
    }

    public static UserResponseDTO toResponse(UserEntity entity) {
        // converting AccountRole to String type
        String role = entity.getRole() != null ? entity.getRole().toString() : null;
        return UserResponseDTO.builder()
                .userId(entity.getUserId())
                .userName(entity.getUserName())
                .email(entity.getEmail())
                .role(role)
                .address(entity.getAddress() == null ? null : AddressMapper.toDTO(entity.getAddress()))
                .build();
    }
}
