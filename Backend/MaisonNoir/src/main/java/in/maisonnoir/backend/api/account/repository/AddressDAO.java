package in.maisonnoir.backend.api.account.repository;

import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressDAO extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByUserId(Long userId);
}
