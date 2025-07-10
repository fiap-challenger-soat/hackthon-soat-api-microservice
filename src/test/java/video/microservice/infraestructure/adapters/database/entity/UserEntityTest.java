package video.microservice.infraestructure.adapters.database.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class UserEntityTest {

    @Test
    public void testUserEntity(){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("teste@example");
        userEntity.setId(UUID.randomUUID());
        userEntity.setCreatedAt(OffsetDateTime.now());
        userEntity.setVideoJobs(new ArrayList<>());

        Assertions.assertEquals("teste@example", userEntity.getEmail());
        Assertions.assertNotNull(userEntity.getId());
        Assertions.assertNotNull(userEntity.getCreatedAt());
        Assertions.assertNotNull(userEntity.getVideoJobs());
    }
}
