package payment.microservice.infraestructure.adapters.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
public class UserEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoEntity> videoJobs;
}
