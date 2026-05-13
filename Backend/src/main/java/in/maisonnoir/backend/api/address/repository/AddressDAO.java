package in.maisonnoir.backend.api.address.repository;

import in.maisonnoir.backend.api.address.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressDAO extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByUserId(Long userId);
}
