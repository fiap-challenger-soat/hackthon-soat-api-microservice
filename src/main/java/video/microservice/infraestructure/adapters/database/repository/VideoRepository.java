package video.microservice.infraestructure.adapters.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import video.microservice.infraestructure.adapters.database.entity.VideoEntity;

import java.util.Optional;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoEntity, UUID> {
        Optional<VideoEntity> findByIdAndUserEmail(UUID id, String email);
    }
