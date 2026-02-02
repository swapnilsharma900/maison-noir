package in.maisonnoir.backend.api.account.repository;

import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressDAO extends JpaRepository<AddressEntity, Long> {}
