package in.maisonnoir.backend.api.account.repository;

import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressDAO extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByUserId(Long id);
}
