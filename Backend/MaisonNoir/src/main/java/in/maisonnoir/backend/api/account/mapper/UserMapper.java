package in.maisonnoir.backend.api.account.mapper;

import in.maisonnoir.backend.api.account.model.dto.user.UserResponseDTO;
import in.maisonnoir.backend.api.account.model.dto.user.UserUpdateDTO;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;

public class UserMapper {

    public static boolean applyUpdate(UserUpdateDTO dto, UserEntity entity) {
        boolean changed = false;

        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            entity.setFirstName(dto.getFirstName());
            changed = true;
        }
        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            entity.setLastName(dto.getLastName());
            changed = true;
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            entity.setEmail(dto.getEmail());
            changed = true;
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
            changed = true;
        }
        return changed;
    }

    public static UserResponseDTO toResponse(UserEntity entity) {
        String role = entity.getRole() != null ? entity.getRole().toString() : null;
        return UserResponseDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .role(role)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
