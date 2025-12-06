package in.maisonnoir.backend.api.account.repository;

import in.maisonnoir.backend.api.account.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
