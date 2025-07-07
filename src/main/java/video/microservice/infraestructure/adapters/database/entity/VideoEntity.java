package payment.microservice.infraestructure.adapters.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_video_jobs")
@Getter
@Setter
public class VideoEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "video_path", length = 255)
    private String videoPath;

    @Column(name = "output_path", length = 255)
    private String outputPath;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;
}
