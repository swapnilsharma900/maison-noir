package in.maisonnoir.backend.api.account.repository;

import in.maisonnoir.backend.api.account.model.entity.AddressEntity;
import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    long countByAddress(AddressEntity address);
}
