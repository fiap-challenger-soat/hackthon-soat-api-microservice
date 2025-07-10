package video.microservice.infraestructure.adapters.database.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

public class VideoEntityTest {
    @Test
    public void testUserEntity(){
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setUser(new UserEntity());
        videoEntity.setStatus("QUEUED");
        videoEntity.setVideoPath("videoPath");
        videoEntity.setId(UUID.randomUUID());
        videoEntity.setCreatedAt(OffsetDateTime.now());
        videoEntity.setOutputPath("teste");

        Assertions.assertEquals("QUEUED", videoEntity.getStatus());
        Assertions.assertEquals("videoPath", videoEntity.getVideoPath());
        Assertions.assertEquals("teste", videoEntity.getOutputPath());

        Assertions.assertNotNull(videoEntity.getId());
        Assertions.assertNotNull(videoEntity.getCreatedAt());
        Assertions.assertNotNull(videoEntity.getUser());
    }
}
