package in.maisonnoir.backend.api.account.mapper;

import in.maisonnoir.backend.api.account.model.dto.AddressDTO;
import in.maisonnoir.backend.api.account.model.entity.AddressEntity;

public class AddressMapper {
    public static AddressDTO toDTO(AddressEntity entity) {
        if (entity == null) return null;

        return AddressDTO.builder()
                .street(entity.getStreet())
                .city(entity.getCity())
                .state(entity.getState())
                .postalCode(entity.getPostalCode())
                .country(entity.getCountry())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .isDefault(entity.isDefault())
                .build();

    }

    public static AddressEntity toEntity(AddressDTO dto) {
        if (dto == null) return null;

        return AddressEntity.builder()
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .isDefault(dto.isDefault())
                .build();

    }
}
