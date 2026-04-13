package in.maisonnoir.backend.api.account.mapper;

import in.maisonnoir.backend.api.account.model.dto.address.AddressDTO;
import in.maisonnoir.backend.api.account.model.entity.AddressEntity;

public class AddressMapper {
    public static AddressDTO toDTO(AddressEntity entity) {
        if (entity == null) return null;

        return AddressDTO.builder()
                .lineOne(entity.getLineOne())
                .lineTwo(entity.getLineTwo())
                .landmark(entity.getLandmark())
                .city(entity.getCity())
                .state(entity.getState())
                .pincode(entity.getPincode())
                .country(entity.getCountry())
                .build();
    }

    public static AddressEntity toEntity(AddressDTO dto) {
        if (dto == null) return null;

        return AddressEntity.builder()
                .lineOne(dto.getLineOne())
                .lineTwo(dto.getLineTwo())
                .landmark(dto.getLandmark())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .country(dto.getCountry())
                .build();
    }
}
