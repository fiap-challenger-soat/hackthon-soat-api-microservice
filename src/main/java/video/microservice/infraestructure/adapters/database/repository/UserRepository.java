package payment.microservice.infraestructure.adapters.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment.microservice.infraestructure.adapters.database.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
