package in.maisonnoir.backend.api.account.mapper;

import in.maisonnoir.backend.api.account.model.dto.UserDTO;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(UserDTO dto) {
        return UserEntity.builder()
                .userId(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    public static UserDTO toDTO(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getUserId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }

}
