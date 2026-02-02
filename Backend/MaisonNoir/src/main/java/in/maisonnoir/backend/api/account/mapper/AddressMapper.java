package in.maisonnoir.backend.api.account.mapper;

import in.maisonnoir.backend.api.account.model.dto.address.AddressDTO;
import in.maisonnoir.backend.api.account.model.entity.AddressEntity;

public class AddressMapper {
    public static AddressDTO toDTO(AddressEntity entity) {
        if (entity == null) return null;

        return AddressDTO.builder()
                .mainLine(entity.getMainLine())
                .locality(entity.getLocality())
                .landmark(entity.getLandmark())
                .city(entity.getCity())
                .state(entity.getState())
                .postalCode(entity.getPostalCode())
                .country(entity.getCountry())
                .build();
    }

    public static AddressEntity toEntity(AddressDTO dto) {
        if (dto == null) return null;

        return AddressEntity.builder()
                .mainLine(dto.getMainLine())
                .locality(dto.getLocality())
                .landmark(dto.getLandmark())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .build();
    }
}
