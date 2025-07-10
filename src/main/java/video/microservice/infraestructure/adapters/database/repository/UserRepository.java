package video.microservice.infraestructure.adapters.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import video.microservice.infraestructure.adapters.database.entity.UserEntity;
import video.microservice.infraestructure.adapters.database.entity.VideoEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
}
